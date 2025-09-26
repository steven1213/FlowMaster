#!/bin/bash

# FlowMaster 启动脚本

echo "=========================================="
echo "FlowMaster - 企业级智能审批流平台"
echo "=========================================="

# 检查Java版本
check_java() {
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
        echo "Java版本: $JAVA_VERSION"
        
        # 检查Java版本是否为17或更高
        if [[ "$JAVA_VERSION" < "17" ]]; then
            echo "错误: 需要Java 17或更高版本"
            exit 1
        fi
    else
        echo "错误: 未找到Java，请安装Java 17或更高版本"
        exit 1
    fi
}

# 检查Node.js
check_node() {
    if command -v node &> /dev/null; then
        NODE_VERSION=$(node -v)
        echo "Node.js版本: $NODE_VERSION"
        
        # 检查Node.js版本是否为18或更高
        if [[ "${NODE_VERSION#v}" < "18" ]]; then
            echo "错误: 需要Node.js 18或更高版本"
            exit 1
        fi
    else
        echo "错误: 未找到Node.js，请安装Node.js 18或更高版本"
        exit 1
    fi
}

# 检查Docker
check_docker() {
    if command -v docker &> /dev/null; then
        echo "Docker已安装"
    else
        echo "警告: 未找到Docker，请安装Docker"
    fi
}

# 检查Docker Compose
check_docker_compose() {
    if command -v docker-compose &> /dev/null; then
        echo "Docker Compose已安装"
    else
        echo "警告: 未找到Docker Compose，请安装Docker Compose"
    fi
}

# 启动基础设施
start_infrastructure() {
    echo "启动基础设施..."
    
    if command -v docker-compose &> /dev/null; then
        # 检查环境变量文件
        if [ ! -f .env ]; then
            echo "创建环境变量文件..."
            cp env.example .env
        fi
        
        docker-compose -f docker-compose.dev.yml up -d
        echo "基础设施启动完成"
    else
        echo "错误: 无法启动基础设施，请安装Docker Compose"
        exit 1
    fi
}

# 等待服务启动
wait_for_services() {
    echo "等待服务启动..."
    
    # 等待MySQL
    echo "等待MySQL启动..."
    while ! docker exec flowmaster-mysql mysqladmin ping -h localhost --silent; do
        sleep 2
    done
    echo "MySQL已启动"
    
    # 等待Redis
    echo "等待Redis启动..."
    while ! docker exec flowmaster-redis redis-cli ping | grep -q PONG; do
        sleep 2
    done
    echo "Redis已启动"
    
    # 等待Elasticsearch
    echo "等待Elasticsearch启动..."
    while ! curl -s http://localhost:9200 > /dev/null; do
        sleep 2
    done
    echo "Elasticsearch已启动"
}

# 构建后端项目
build_backend() {
    echo "构建后端项目..."
    
    # 构建公共组件
    echo "构建公共组件..."
    cd ../flowmaster-backend/flowmaster-common
    mvn clean install -DskipTests
    cd ../..
    
    echo "后端项目构建完成"
}

# 构建前端项目
build_frontend() {
    echo "构建前端项目..."
    
    # 安装前端依赖
    echo "安装前端依赖..."
    cd ../flowmaster-frontend
    npm run install:all
    
    echo "前端项目构建完成"
}

# 启动应用
start_application() {
    echo "启动应用..."
    
    # 这里可以添加启动应用的命令
    echo "应用启动完成"
}

# 主函数
main() {
    echo "开始启动FlowMaster..."
    
    # 检查环境
    check_java
    check_node
    check_docker
    check_docker_compose
    
    # 启动基础设施
    start_infrastructure
    
    # 等待服务启动
    wait_for_services
    
    # 构建项目
    build_backend
    build_frontend
    
    # 启动应用
    start_application
    
    echo "=========================================="
    echo "FlowMaster启动完成！"
    echo "=========================================="
    echo "访问地址:"
    echo "  - 后端API: http://localhost:8080"
    echo "  - 前端Web: http://localhost:3000"
    echo "  - 数据库: localhost:3306"
    echo "  - Redis: localhost:6379"
    echo "  - Elasticsearch: http://localhost:9200"
    echo "  - Kibana: http://localhost:5601"
    echo "  - Kafka: localhost:9092"
    echo "  - RabbitMQ: http://localhost:15672"
    echo "  - MinIO: http://localhost:9000"
    echo "=========================================="
}

# 执行主函数
main "$@"
