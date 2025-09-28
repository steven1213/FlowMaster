@echo off
REM FlowMaster 后端服务一键启动脚本 (Windows版本)
REM 作者: FlowMaster Team
REM 版本: 1.0.0

setlocal enabledelayedexpansion

REM 项目根目录
set "PROJECT_ROOT=C:\Users\steven\Project\FlowMaster"
set "BACKEND_DIR=%PROJECT_ROOT%\flowmaster-backend"
set "SCRIPTS_DIR=%PROJECT_ROOT%\scripts"
set "LOGS_DIR=%PROJECT_ROOT%\logs"

REM 服务配置
set "SERVICES=registry:8761 user:8081 auth:8082 workflow:8083 monitoring:8085 gateway:8080"

REM 启动顺序
set "START_ORDER=registry user auth workflow monitoring gateway"

REM 启动超时时间（秒）
set "START_TIMEOUT=60"
set "HEALTH_CHECK_INTERVAL=5"

echo.
echo ========================================
echo    FlowMaster 后端服务管理脚本
echo ========================================
echo.

REM 检查参数
if "%1"=="" set "ACTION=start"
if "%1"=="start" set "ACTION=start"
if "%1"=="stop" set "ACTION=stop"
if "%1"=="restart" set "ACTION=restart"
if "%1"=="status" set "ACTION=status"
if "%1"=="help" set "ACTION=help"
if "%1"=="-h" set "ACTION=help"
if "%1"=="--help" set "ACTION=help"

if "%ACTION%"=="help" goto :show_help
if "%ACTION%"=="start" goto :start_services
if "%ACTION%"=="stop" goto :stop_services
if "%ACTION%"=="restart" goto :restart_services
if "%ACTION%"=="status" goto :show_status

echo 错误: 未知选项 %1
goto :show_help

:show_help
echo 用法: %0 [选项]
echo.
echo 选项:
echo   start     启动所有后端服务
echo   stop      停止所有后端服务
echo   restart   重启所有后端服务
echo   status    显示服务状态
echo   help      显示此帮助信息
echo.
echo 示例:
echo   %0 start     # 启动所有服务
echo   %0 stop      # 停止所有服务
echo   %0 status    # 查看服务状态
echo.
goto :end

:check_dependencies
echo [INFO] 检查系统依赖...

REM 检查Java
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java 未安装或未在PATH中
    exit /b 1
)

REM 检查Maven
mvn -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Maven 未安装或未在PATH中
    exit /b 1
)

echo [SUCCESS] 依赖检查完成
goto :eof

:create_directories
echo [INFO] 创建必要目录...
if not exist "%LOGS_DIR%" mkdir "%LOGS_DIR%"
if not exist "%LOGS_DIR%\services" mkdir "%LOGS_DIR%\services"
echo [SUCCESS] 目录创建完成
goto :eof

:build_project
echo [INFO] 编译后端项目...
cd /d "%BACKEND_DIR%"
mvn clean package -DskipTests -q
if errorlevel 1 (
    echo [ERROR] 项目编译失败
    exit /b 1
)
echo [SUCCESS] 项目编译成功
goto :eof

:check_service_health
set "SERVICE_NAME=%1"
set "PORT=%2"
set "MAX_ATTEMPTS=12"
set "ATTEMPT=1"

echo [INFO] 检查 %SERVICE_NAME% 服务健康状态...

:health_check_loop
curl -s "http://localhost:%PORT%/actuator/health" >nul 2>&1
if not errorlevel 1 (
    echo [SUCCESS] %SERVICE_NAME% 服务启动成功 (端口: %PORT%)
    goto :eof
)

echo [INFO] 等待 %SERVICE_NAME% 服务启动... (!ATTEMPT!/!MAX_ATTEMPTS!)
timeout /t %HEALTH_CHECK_INTERVAL% /nobreak >nul
set /a ATTEMPT+=1
if !ATTEMPT! leq !MAX_ATTEMPTS! goto :health_check_loop

echo [ERROR] %SERVICE_NAME% 服务启动超时
exit /b 1

:start_service
set "SERVICE_NAME=%1"
set "SERVICE_DIR=%BACKEND_DIR%\%SERVICE_NAME%"
set "JAR_FILE=%SERVICE_DIR%\target\%SERVICE_NAME%-1.0.0-SNAPSHOT.jar"
set "LOG_FILE=%LOGS_DIR%\services\%SERVICE_NAME%.log"
set "PID_FILE=%LOGS_DIR%\services\%SERVICE_NAME%.pid"

echo [INFO] 启动 %SERVICE_NAME% 服务...

REM 检查服务目录是否存在
if not exist "%SERVICE_DIR%" (
    echo [ERROR] 服务目录不存在: %SERVICE_DIR%
    exit /b 1
)

REM 检查JAR文件是否存在
if not exist "%JAR_FILE%" (
    echo [ERROR] JAR文件不存在: %JAR_FILE%
    exit /b 1
)

REM 启动服务
cd /d "%SERVICE_DIR%"
start /b java -jar "%JAR_FILE%" --spring.profiles.active=dev > "%LOG_FILE%" 2>&1

REM 等待服务启动
timeout /t 3 /nobreak >nul

REM 获取服务端口
for %%s in (%SERVICES%) do (
    for /f "tokens=1,2 delims=:" %%a in ("%%s") do (
        if "%%a"=="%SERVICE_NAME%" (
            call :check_service_health "%SERVICE_NAME%" "%%b"
            if errorlevel 1 exit /b 1
        )
    )
)

echo [SUCCESS] %SERVICE_NAME% 服务启动成功
goto :eof

:start_services
call :check_dependencies
if errorlevel 1 exit /b 1

call :create_directories
call :build_project
if errorlevel 1 exit /b 1

echo [INFO] 开始启动所有后端服务...

for %%s in (%START_ORDER%) do (
    call :start_service "%%s"
    if errorlevel 1 (
        echo [ERROR] %%s 服务启动失败，停止后续服务启动
        call :stop_services
        exit /b 1
    )
    echo [SUCCESS] %%s 服务启动成功
    timeout /t 3 /nobreak >nul
)

echo [SUCCESS] 所有后端服务启动完成！
call :show_status
goto :end

:stop_services
echo [INFO] 停止所有后端服务...

REM 按启动顺序的逆序停止服务
for %%s in (gateway monitoring workflow auth user registry) do (
    set "PID_FILE=%LOGS_DIR%\services\%%s.pid"
    if exist "!PID_FILE!" (
        echo [INFO] 停止 %%s 服务...
        REM 查找并停止Java进程
        for /f "tokens=2" %%p in ('tasklist /fi "imagename eq java.exe" /fo csv ^| findstr /i "%%s"') do (
            taskkill /f /pid %%p >nul 2>&1
        )
        del "!PID_FILE!" >nul 2>&1
        echo [SUCCESS] %%s 服务已停止
    )
)

echo [SUCCESS] 所有后端服务已停止
call :show_status
goto :end

:restart_services
call :stop_services
timeout /t 5 /nobreak >nul
call :start_services
goto :end

:show_status
echo.
echo [INFO] 服务状态检查...
echo.
echo 服务名称              端口        状态
echo ----------------------------------------

for %%s in (%SERVICES%) do (
    for /f "tokens=1,2 delims=:" %%a in ("%%s") do (
        set "SERVICE_NAME=%%a"
        set "PORT=%%b"
        
        REM 检查服务是否运行
        curl -s "http://localhost:!PORT!/actuator/health" >nul 2>&1
        if not errorlevel 1 (
            set "STATUS=运行中"
        ) else (
            set "STATUS=未运行"
        )
        
        echo !SERVICE_NAME!              !PORT!        !STATUS!
    )
)
echo.
goto :eof

:end
echo.
echo 脚本执行完成
pause
