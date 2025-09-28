#!/bin/bash

# FlowMaster 后端服务一键启动脚本
# 作者: FlowMaster Team
# 版本: 1.0.0

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 项目根目录
PROJECT_ROOT="/Users/steven/Project/FlowMaster"
BACKEND_DIR="$PROJECT_ROOT/flowmaster-backend"
SCRIPTS_DIR="$PROJECT_ROOT/scripts"
LOGS_DIR="$PROJECT_ROOT/logs"

# 服务配置
SERVICES="flowmaster-registry:flowmaster-registry:8761 flowmaster-user:flowmaster-user:8081 flowmaster-auth:flowmaster-auth:8082 flowmaster-workflow:flowmaster-workflow:8083 flowmaster-monitoring:flowmaster-monitoring:8085 flowmaster-gateway:flowmaster-gateway:8080"

# 启动顺序（按依赖关系排序）
START_ORDER="flowmaster-registry flowmaster-user flowmaster-auth flowmaster-workflow flowmaster-monitoring flowmaster-gateway"

# 启动超时时间（秒）
START_TIMEOUT=60
HEALTH_CHECK_INTERVAL=5

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

# 检查依赖
check_dependencies() {
    log_info "检查系统依赖..."
    
    # 检查Java
    if ! command -v java &> /dev/null; then
        log_error "Java 未安装或未在PATH中"
        exit 1
    fi
    
    # 检查Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven 未安装或未在PATH中"
        exit 1
    fi
    
    # 检查端口是否被占用
    for service_info in $SERVICES; do
        port=$(echo "$service_info" | cut -d':' -f3)
        if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
            log_warning "端口 $port 已被占用，请先停止相关服务"
        fi
    done
    
    log_success "依赖检查完成"
}

# 创建必要目录
create_directories() {
    log_info "创建必要目录..."
    mkdir -p "$LOGS_DIR"
    mkdir -p "$LOGS_DIR/services"
    log_success "目录创建完成"
}

# 编译项目
build_project() {
    log_info "编译后端项目..."
    cd "$BACKEND_DIR"
    
    if mvn clean package -DskipTests -q; then
        log_success "项目编译成功"
    else
        log_error "项目编译失败"
        exit 1
    fi
}

# 检查服务健康状态
check_service_health() {
    local service_name=$1
    local port=$2
    local max_attempts=$((START_TIMEOUT / HEALTH_CHECK_INTERVAL))
    local attempt=1
    
    log_info "检查 $service_name 服务健康状态..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "http://localhost:$port/actuator/health" >/dev/null 2>&1; then
            log_success "$service_name 服务启动成功 (端口: $port)"
            return 0
        fi
        
        log_info "等待 $service_name 服务启动... ($attempt/$max_attempts)"
        sleep $HEALTH_CHECK_INTERVAL
        ((attempt++))
    done
    
    log_error "$service_name 服务启动超时"
    return 1
}

# 启动单个服务
start_service() {
    local service_name=$1
    local service_info=""
    local port=""
    
    # 查找服务信息
    for service in $SERVICES; do
        if echo "$service" | grep -q "^$service_name:"; then
            service_info="$service"
            port=$(echo "$service" | cut -d':' -f3)
            break
        fi
    done
    
    if [ -z "$service_info" ]; then
        log_error "未找到服务: $service_name"
        return 1
    fi
    
    local service_dir="$BACKEND_DIR/$service_name"
    local log_file="$LOGS_DIR/services/$service_name.log"
    
    log_info "启动 $service_name 服务..."
    
    # 检查服务目录是否存在
    if [ ! -d "$service_dir" ]; then
        log_error "服务目录不存在: $service_dir"
        return 1
    fi
    
    # 检查JAR文件是否存在
    local jar_file="$service_dir/target/$service_name-1.0.0-SNAPSHOT.jar"
    if [ ! -f "$jar_file" ]; then
        log_error "JAR文件不存在: $jar_file"
        return 1
    fi
    
    # 启动服务
    cd "$service_dir"
    nohup java -jar "$jar_file" \
        --spring.profiles.active=dev \
        --server.port=$port \
        > "$log_file" 2>&1 &
    
    local pid=$!
    echo $pid > "$LOGS_DIR/services/$service_name.pid"
    
    log_info "$service_name 服务已启动 (PID: $pid, 端口: $port)"
    
    # 等待服务健康检查
    if check_service_health "$service_name" "$port"; then
        return 0
    else
        log_error "$service_name 服务启动失败"
        return 1
    fi
}

# 启动所有服务
start_all_services() {
    log_info "开始启动所有后端服务..."
    
    for service in $START_ORDER; do
        if start_service "$service"; then
            log_success "$service 服务启动成功"
        else
            log_error "$service 服务启动失败，停止后续服务启动"
            stop_all_services
            exit 1
        fi
        
        # 服务间启动间隔
        sleep 3
    done
    
    log_success "所有后端服务启动完成！"
}

# 停止所有服务
stop_all_services() {
    log_info "停止所有后端服务..."
    
    # 按启动顺序的逆序停止服务
    for service in flowmaster-gateway flowmaster-monitoring flowmaster-workflow flowmaster-auth flowmaster-user flowmaster-registry; do
        local pid_file="$LOGS_DIR/services/$service.pid"
        
        if [ -f "$pid_file" ]; then
            local pid=$(cat "$pid_file")
            if ps -p $pid > /dev/null 2>&1; then
                log_info "停止 $service 服务 (PID: $pid)..."
                kill $pid
                
                # 等待进程结束
                local count=0
                while ps -p $pid > /dev/null 2>&1 && [ $count -lt 10 ]; do
                    sleep 1
                    ((count++))
                done
                
                # 如果进程仍在运行，强制杀死
                if ps -p $pid > /dev/null 2>&1; then
                    log_warning "强制停止 $service 服务"
                    kill -9 $pid
                fi
                
                log_success "$service 服务已停止"
            else
                log_warning "$service 服务进程不存在"
            fi
            
            rm -f "$pid_file"
        else
            log_warning "$service 服务PID文件不存在"
        fi
    done
    
    log_success "所有后端服务已停止"
}

# 显示服务状态
show_status() {
    log_info "服务状态检查..."
    echo
    printf "%-20s %-10s %-10s %-20s\n" "服务名称" "端口" "状态" "PID"
    printf "%-20s %-10s %-10s %-20s\n" "--------------------" "----------" "----------" "--------------------"
    
    for service_info in $SERVICES; do
        local service_name=$(echo "$service_info" | cut -d':' -f1)
        local port=$(echo "$service_info" | cut -d':' -f3)
        local pid_file="$LOGS_DIR/services/$service_name.pid"
        local status="未运行"
        local pid="N/A"
        
        if [ -f "$pid_file" ]; then
            local service_pid=$(cat "$pid_file")
            if ps -p $service_pid > /dev/null 2>&1; then
                if curl -s "http://localhost:$port/actuator/health" >/dev/null 2>&1; then
                    status="运行中"
                    pid=$service_pid
                else
                    status="启动中"
                    pid=$service_pid
                fi
            fi
        fi
        
        printf "%-20s %-10s %-10s %-20s\n" "$service_name" "$port" "$status" "$pid"
    done
    echo
}

# 清理日志
clean_logs() {
    log_info "清理服务日志..."
    rm -rf "$LOGS_DIR/services"
    mkdir -p "$LOGS_DIR/services"
    log_success "日志清理完成"
}

# 显示帮助信息
show_help() {
    echo "FlowMaster 后端服务管理脚本"
    echo
    echo "用法: $0 [选项]"
    echo
    echo "选项:"
    echo "  start     启动所有后端服务"
    echo "  stop      停止所有后端服务"
    echo "  restart   重启所有后端服务"
    echo "  status    显示服务状态"
    echo "  logs      查看服务日志"
    echo "  clean     清理服务日志"
    echo "  help      显示此帮助信息"
    echo
    echo "示例:"
    echo "  $0 start     # 启动所有服务"
    echo "  $0 stop      # 停止所有服务"
    echo "  $0 status    # 查看服务状态"
    echo
}

# 查看服务日志
view_logs() {
    local service=$1
    if [ -z "$service" ]; then
        log_info "可用的服务日志:"
        for service_info in $SERVICES; do
            local service_name=$(echo "$service_info" | cut -d':' -f1)
            echo "  - $service_name"
        done
        echo
        echo "用法: $0 logs <服务名>"
        return 0
    fi
    
    local log_file="$LOGS_DIR/services/$service.log"
    if [ -f "$log_file" ]; then
        log_info "显示 $service 服务日志 (最后50行):"
        echo "----------------------------------------"
        tail -50 "$log_file"
        echo "----------------------------------------"
        echo "完整日志文件: $log_file"
    else
        log_error "$service 服务日志文件不存在: $log_file"
    fi
}

# 主函数
main() {
    case "${1:-start}" in
        "start")
            check_dependencies
            create_directories
            build_project
            start_all_services
            show_status
            ;;
        "stop")
            stop_all_services
            show_status
            ;;
        "restart")
            stop_all_services
            sleep 5
            check_dependencies
            create_directories
            build_project
            start_all_services
            show_status
            ;;
        "status")
            show_status
            ;;
        "logs")
            view_logs "$2"
            ;;
        "clean")
            clean_logs
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            log_error "未知选项: $1"
            show_help
            exit 1
            ;;
    esac
}

# 捕获中断信号
trap 'log_warning "收到中断信号，正在停止所有服务..."; stop_all_services; exit 0' INT TERM

# 执行主函数
main "$@"
