@echo off
chcp 65001 >nul
SETLOCAL

echo ========================================
echo    FlowMaster Core Services Startup
echo ========================================

echo [INFO] Starting core services only...

REM Start User Service
echo.
echo [INFO] Starting User Service...
if exist "flowmaster-user" (
    cd flowmaster-user
    echo [INFO] Compiling User Service...
    mvn clean compile -q
    if %ERRORLEVEL% EQU 0 (
        echo [INFO] Starting User Service on port 8081...
        start "FlowMaster-User" cmd /k "mvn spring-boot:run"
        echo [SUCCESS] User Service started
    ) else (
        echo [ERROR] User Service compilation failed
    )
    cd ..
) else (
    echo [ERROR] flowmaster-user directory not found
)

REM Wait a bit
timeout /t 3 /nobreak > NUL

REM Start Auth Service
echo.
echo [INFO] Starting Auth Service...
if exist "flowmaster-auth" (
    cd flowmaster-auth
    echo [INFO] Compiling Auth Service...
    mvn clean compile -q
    if %ERRORLEVEL% EQU 0 (
        echo [INFO] Starting Auth Service on port 8082...
        start "FlowMaster-Auth" cmd /k "mvn spring-boot:run"
        echo [SUCCESS] Auth Service started
    ) else (
        echo [ERROR] Auth Service compilation failed
    )
    cd ..
) else (
    echo [ERROR] flowmaster-auth directory not found
)

REM Wait a bit
timeout /t 3 /nobreak > NUL

REM Start Workflow Service
echo.
echo [INFO] Starting Workflow Service...
if exist "flowmaster-workflow" (
    cd flowmaster-workflow
    echo [INFO] Compiling Workflow Service...
    mvn clean compile -q
    if %ERRORLEVEL% EQU 0 (
        echo [INFO] Starting Workflow Service on port 8083...
        start "FlowMaster-Workflow" cmd /k "mvn spring-boot:run"
        echo [SUCCESS] Workflow Service started
    ) else (
        echo [ERROR] Workflow Service compilation failed
    )
    cd ..
) else (
    echo [ERROR] flowmaster-workflow directory not found
)

echo.
echo ========================================
echo [SUCCESS] Core services startup completed!
echo ========================================
echo.
echo Service URLs:
echo   User Service:     http://localhost:8081/user-service
echo   Auth Service:     http://localhost:8082/auth-service
echo   Workflow Service: http://localhost:8083/workflow-service
echo.
echo API Documentation:
echo   User Service:     http://localhost:8081/user-service/swagger-ui.html
echo   Auth Service:     http://localhost:8082/auth-service/swagger-ui.html
echo   Workflow Service: http://localhost:8083/workflow-service/swagger-ui.html
echo.
echo Note: Each service runs in its own window.
echo Close those windows to stop the services.
echo.
echo Press any key to exit...
pause > NUL

ENDLOCAL
