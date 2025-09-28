#!/bin/bash

# FlowMaster 后端服务一键停止脚本
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
LOGS_DIR="$PROJECT_ROOT/logs"

# 服务配置
SERVICES="flowmaster-registry:flowmaster-registry:8761 flowmaster-user:flowmaster-user:8081 flowmaster-auth:flowmaster-auth:8082 flowmaster-workflow:flowmaster-workflow:8083 flowmaster-monitoring:flowmaster-monitoring:8085 flowmaster-gateway:flowmaster-gateway:8080"

# 停止顺序（按启动顺序的逆序）
STOP_ORDER="flowmaster-gateway flowmaster-monitoring flowmaster-workflow flowmaster-auth flowmaster-user flowmaster-registry"

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

# 停止单个服务
stop_service() {
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
    
    local pid_file="$LOGS_DIR/services/$service_name.pid"
    
    log_info "停止 $service_name 服务..."
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p $pid > /dev/null 2>&1; then
            log_info "找到 $service_name 服务进程 (PID: $pid)"
            
            # 优雅停止
            kill $pid
            log_info "发送停止信号给 $service_name 服务..."
            
            # 等待进程结束
            local count=0
            while ps -p $pid > /dev/null 2>&1 && [ $count -lt 10 ]; do
                sleep 1
                ((count++))
                log_info "等待 $service_name 服务停止... ($count/10)"
            done
            
            # 如果进程仍在运行，强制杀死
            if ps -p $pid > /dev/null 2>&1; then
                log_warning "$service_name 服务未响应停止信号，强制停止"
                kill -9 $pid
                sleep 2
                
                if ps -p $pid > /dev/null 2>&1; then
                    log_error "无法停止 $service_name 服务 (PID: $pid)"
                    return 1
                fi
            fi
            
            log_success "$service_name 服务已停止"
        else
            log_warning "$service_name 服务进程不存在 (PID: $pid)"
        fi
        
        # 删除PID文件
        rm -f "$pid_file"
    else
        log_warning "$service_name 服务PID文件不存在，尝试通过端口查找进程"
        
        # 通过端口查找并停止进程
        local port_pid=$(lsof -ti:$port 2>/dev/null || echo "")
        if [ -n "$port_pid" ]; then
            log_info "通过端口 $port 找到进程 (PID: $port_pid)"
            kill $port_pid
            sleep 2
            
            if ps -p $port_pid > /dev/null 2>&1; then
                kill -9 $port_pid
            fi
            
            log_success "$service_name 服务已停止"
        else
            log_warning "未找到占用端口 $port 的进程"
        fi
    fi
    
    return 0
}

# 停止所有服务
stop_all_services() {
    log_info "开始停止所有后端服务..."
    
    for service in $STOP_ORDER; do
        if stop_service "$service"; then
            log_success "$service 服务停止成功"
        else
            log_error "$service 服务停止失败"
        fi
        
        # 服务间停止间隔
        sleep 2
    done
    
    log_success "所有后端服务停止完成！"
}

# 强制停止所有Java进程
force_stop_all_java() {
    log_warning "强制停止所有Java进程..."
    
    # 查找所有FlowMaster相关的Java进程
    local java_pids=$(ps aux | grep java | grep -E "(flowmaster|FlowMaster)" | grep -v grep | awk '{print $2}' || echo "")
    
    if [ -n "$java_pids" ]; then
        log_info "找到以下Java进程: $java_pids"
        for pid in $java_pids; do
            if ps -p $pid > /dev/null 2>&1; then
                log_info "强制停止进程 $pid"
                kill -9 $pid
            fi
        done
        log_success "所有FlowMaster Java进程已强制停止"
    else
        log_info "未找到FlowMaster相关的Java进程"
    fi
}

# 清理端口
cleanup_ports() {
    log_info "清理被占用的端口..."
    
    for service_info in $SERVICES; do
        local port=$(echo "$service_info" | cut -d':' -f3)
        local port_pid=$(lsof -ti:$port 2>/dev/null || echo "")
        
        if [ -n "$port_pid" ]; then
            log_warning "端口 $port 仍被进程 $port_pid 占用，强制清理"
            kill -9 $port_pid 2>/dev/null || true
        fi
    done
    
    log_success "端口清理完成"
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
                    status="停止中"
                    pid=$service_pid
                fi
            fi
        else
            # 检查端口是否被占用
            local port_pid=$(lsof -ti:$port 2>/dev/null || echo "")
            if [ -n "$port_pid" ]; then
                status="端口占用"
                pid=$port_pid
            fi
        fi
        
        printf "%-20s %-10s %-10s %-20s\n" "$service_name" "$port" "$status" "$pid"
    done
    echo
}

# 清理日志和临时文件
cleanup_files() {
    log_info "清理服务日志和临时文件..."
    
    if [ -d "$LOGS_DIR/services" ]; then
        rm -rf "$LOGS_DIR/services"
        log_success "服务日志已清理"
    fi
    
    # 清理临时文件
    find /tmp -name "*flowmaster*" -type f -delete 2>/dev/null || true
    find /tmp -name "*FlowMaster*" -type f -delete 2>/dev/null || true
    
    log_success "临时文件清理完成"
}

# 显示帮助信息
show_help() {
    echo "FlowMaster 后端服务停止脚本"
    echo
    echo "用法: $0 [选项]"
    echo
    echo "选项:"
    echo "  stop      停止所有后端服务 (默认)"
    echo "  force     强制停止所有Java进程"
    echo "  cleanup   清理端口和临时文件"
    echo "  status    显示服务状态"
    echo "  clean     清理日志和临时文件"
    echo "  help      显示此帮助信息"
    echo
    echo "示例:"
    echo "  $0 stop     # 停止所有服务"
    echo "  $0 force    # 强制停止所有Java进程"
    echo "  $0 status   # 查看服务状态"
    echo
}

# 主函数
main() {
    case "${1:-stop}" in
        "stop")
            stop_all_services
            show_status
            ;;
        "force")
            force_stop_all_java
            cleanup_ports
            show_status
            ;;
        "cleanup")
            cleanup_ports
            show_status
            ;;
        "status")
            show_status
            ;;
        "clean")
            cleanup_files
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
trap 'log_warning "收到中断信号，正在强制停止所有服务..."; force_stop_all_java; exit 0' INT TERM

# 执行主函数
main "$@"
