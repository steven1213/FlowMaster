@echo off
chcp 65001 >nul

echo ==========================================
echo FlowMaster - 企业级智能审批流平台
echo ==========================================

REM 检查Java版本
:check_java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java，请安装Java 17或更高版本
    pause
    exit /b 1
)

for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)
echo Java版本: %JAVA_VERSION%

REM 检查Node.js
:check_node
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js，请安装Node.js 18或更高版本
    pause
    exit /b 1
)

for /f %%i in ('node -v') do set NODE_VERSION=%%i
echo Node.js版本: %NODE_VERSION%

REM 检查Docker
:check_docker
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 警告: 未找到Docker，请安装Docker
) else (
    echo Docker已安装
)

REM 检查Docker Compose
:check_docker_compose
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 警告: 未找到Docker Compose，请安装Docker Compose
) else (
    echo Docker Compose已安装
)

REM 启动基础设施
:start_infrastructure
echo 启动基础设施...

REM 检查环境变量文件
if not exist .env (
    echo 创建环境变量文件...
    copy env.example .env
)

docker-compose -f docker-compose.dev.yml up -d
if %errorlevel% neq 0 (
    echo 错误: 无法启动基础设施，请检查Docker Compose配置
    pause
    exit /b 1
)
echo 基础设施启动完成

REM 等待服务启动
:wait_for_services
echo 等待服务启动...

REM 等待MySQL
echo 等待MySQL启动...
:wait_mysql
docker exec flowmaster-mysql mysqladmin ping -h localhost --silent >nul 2>&1
if %errorlevel% neq 0 (
    timeout /t 2 >nul
    goto wait_mysql
)
echo MySQL已启动

REM 等待Redis
echo 等待Redis启动...
:wait_redis
docker exec flowmaster-redis redis-cli ping | findstr PONG >nul 2>&1
if %errorlevel% neq 0 (
    timeout /t 2 >nul
    goto wait_redis
)
echo Redis已启动

REM 等待Elasticsearch
echo 等待Elasticsearch启动...
:wait_elasticsearch
curl -s http://localhost:9200 >nul 2>&1
if %errorlevel% neq 0 (
    timeout /t 2 >nul
    goto wait_elasticsearch
)
echo Elasticsearch已启动

REM 构建后端项目
:build_backend
echo 构建后端项目...

echo 构建公共组件...
cd ..\flowmaster-backend\flowmaster-common
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo 错误: 后端项目构建失败
    pause
    exit /b 1
)
cd ..\..

echo 后端项目构建完成

REM 构建前端项目
:build_frontend
echo 构建前端项目...

echo 安装前端依赖...
cd ..\flowmaster-frontend
call npm run install:all
if %errorlevel% neq 0 (
    echo 错误: 前端项目构建失败
    pause
    exit /b 1
)
cd ..\scripts

echo 前端项目构建完成

REM 启动应用
:start_application
echo 启动应用...
echo 应用启动完成

echo ==========================================
echo FlowMaster启动完成！
echo ==========================================
echo 访问地址:
echo   - 后端API: http://localhost:8080
echo   - 前端Web: http://localhost:3000
echo   - 数据库: localhost:3306
echo   - Redis: localhost:6379
echo   - Elasticsearch: http://localhost:9200
echo   - Kibana: http://localhost:5601
echo   - Kafka: localhost:9092
echo   - RabbitMQ: http://localhost:15672
echo   - MinIO: http://localhost:9000
echo ==========================================

pause
