@echo off
REM FlowMaster 后端服务一键停止脚本 (Windows版本)
REM 作者: FlowMaster Team
REM 版本: 1.0.0

setlocal enabledelayedexpansion

REM 项目根目录
set "PROJECT_ROOT=C:\Users\steven\Project\FlowMaster"
set "LOGS_DIR=%PROJECT_ROOT%\logs"

REM 服务配置
set "SERVICES=registry:8761 user:8081 auth:8082 workflow:8083 monitoring:8085 gateway:8080"

REM 停止顺序
set "STOP_ORDER=gateway monitoring workflow auth user registry"

echo.
echo ========================================
echo    FlowMaster 后端服务停止脚本
echo ========================================
echo.

REM 检查参数
if "%1"=="" set "ACTION=stop"
if "%1"=="stop" set "ACTION=stop"
if "%1"=="force" set "ACTION=force"
if "%1"=="cleanup" set "ACTION=cleanup"
if "%1"=="status" set "ACTION=status"
if "%1"=="clean" set "ACTION=clean"
if "%1"=="help" set "ACTION=help"
if "%1"=="-h" set "ACTION=help"
if "%1"=="--help" set "ACTION=help"

if "%ACTION%"=="help" goto :show_help
if "%ACTION%"=="stop" goto :stop_services
if "%ACTION%"=="force" goto :force_stop
if "%ACTION%"=="cleanup" goto :cleanup_ports
if "%ACTION%"=="status" goto :show_status
if "%ACTION%"=="clean" goto :cleanup_files

echo 错误: 未知选项 %1
goto :show_help

:show_help
echo 用法: %0 [选项]
echo.
echo 选项:
echo   stop      停止所有后端服务 (默认)
echo   force     强制停止所有Java进程
echo   cleanup   清理端口和临时文件
echo   status    显示服务状态
echo   clean     清理日志和临时文件
echo   help      显示此帮助信息
echo.
echo 示例:
echo   %0 stop     # 停止所有服务
echo   %0 force    # 强制停止所有Java进程
echo   %0 status   # 查看服务状态
echo.
goto :end

:stop_service
set "SERVICE_NAME=%1"
set "PID_FILE=%LOGS_DIR%\services\%SERVICE_NAME%.pid"

echo [INFO] 停止 %SERVICE_NAME% 服务...

if exist "%PID_FILE%" (
    echo [INFO] 找到 %SERVICE_NAME% 服务PID文件
    del "%PID_FILE%" >nul 2>&1
)

REM 查找并停止Java进程
for /f "tokens=2" %%p in ('tasklist /fi "imagename eq java.exe" /fo csv ^| findstr /i "%SERVICE_NAME%"') do (
    echo [INFO] 停止进程 %%p
    taskkill /f /pid %%p >nul 2>&1
)

REM 通过端口查找并停止进程
for %%s in (%SERVICES%) do (
    for /f "tokens=1,2 delims=:" %%a in ("%%s") do (
        if "%%a"=="%SERVICE_NAME%" (
            set "PORT=%%b"
            for /f "tokens=5" %%p in ('netstat -ano ^| findstr :!PORT!') do (
                if not "%%p"=="0" (
                    echo [INFO] 停止占用端口 !PORT! 的进程 %%p
                    taskkill /f /pid %%p >nul 2>&1
                )
            )
        )
    )
)

echo [SUCCESS] %SERVICE_NAME% 服务已停止
goto :eof

:stop_services
echo [INFO] 开始停止所有后端服务...

for %%s in (%STOP_ORDER%) do (
    call :stop_service "%%s"
    timeout /t 2 /nobreak >nul
)

echo [SUCCESS] 所有后端服务停止完成！
call :show_status
goto :end

:force_stop
echo [WARNING] 强制停止所有Java进程...

REM 查找所有Java进程
for /f "tokens=2" %%p in ('tasklist /fi "imagename eq java.exe" /fo csv') do (
    echo [INFO] 强制停止Java进程 %%p
    taskkill /f /pid %%p >nul 2>&1
)

echo [SUCCESS] 所有Java进程已强制停止
call :cleanup_ports
call :show_status
goto :end

:cleanup_ports
echo [INFO] 清理被占用的端口...

for %%s in (%SERVICES%) do (
    for /f "tokens=1,2 delims=:" %%a in ("%%s") do (
        set "SERVICE_NAME=%%a"
        set "PORT=%%b"
        
        for /f "tokens=5" %%p in ('netstat -ano ^| findstr :!PORT!') do (
            if not "%%p"=="0" (
                echo [WARNING] 端口 !PORT! 仍被进程 %%p 占用，强制清理
                taskkill /f /pid %%p >nul 2>&1
            )
        )
    )
)

echo [SUCCESS] 端口清理完成
goto :eof

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

:cleanup_files
echo [INFO] 清理服务日志和临时文件...

if exist "%LOGS_DIR%\services" (
    rmdir /s /q "%LOGS_DIR%\services"
    echo [SUCCESS] 服务日志已清理
)

REM 清理临时文件
del /q /f "%TEMP%\*flowmaster*" >nul 2>&1
del /q /f "%TEMP%\*FlowMaster*" >nul 2>&1

echo [SUCCESS] 临时文件清理完成
goto :end

:end
echo.
echo 脚本执行完成
pause
