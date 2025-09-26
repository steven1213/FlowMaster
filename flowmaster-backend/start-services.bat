@echo off
chcp 65001 >nul
SETLOCAL

echo ========================================
echo    FlowMaster Backend Services Startup
echo ========================================

REM Check Java environment
java -version > NUL 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java environment not installed or not in PATH
    echo Press any key to exit...
    pause > NUL
    GOTO :EOF
)

REM Check Maven environment
mvn -version > NUL 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven environment not installed or not in PATH
    echo Press any key to exit...
    pause > NUL
    GOTO :EOF
)

echo [INFO] Environment check passed

REM Define service list
SET "SERVICES=flowmaster-user flowmaster-auth flowmaster-workflow flowmaster-gateway flowmaster-monitoring"

REM Start services
FOR %%S IN (%SERVICES%) DO (
    echo.
    echo [INFO] Starting service: %%S
    echo ========================================
    
    REM Check if service directory exists
    IF NOT EXIST "%%S" (
        echo [ERROR] Service directory %%S does not exist
        GOTO :NEXT_SERVICE
    )
    
    REM Enter service directory
    pushd "%%S"
    
    REM Compile service
    echo [INFO] Compiling service %%S...
    mvn clean compile -q
    IF %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Service %%S compilation failed
        popd
        GOTO :NEXT_SERVICE
    )
    
    REM Start service
    echo [INFO] Starting service %%S...
    start "FlowMaster-%%S" cmd /k "mvn spring-boot:run"
    
    REM Return to parent directory
    popd
    
    REM Wait for service startup
    echo [INFO] Waiting for service %%S to start...
    timeout /t 5 /nobreak > NUL
    
    :NEXT_SERVICE
)

echo.
echo ========================================
echo [SUCCESS] All services started successfully!
echo ========================================
echo.
echo Service Access URLs:
echo   User Service:     http://localhost:8081/user-service
echo   Auth Service:     http://localhost:8082/auth-service
echo   Workflow Service: http://localhost:8083/workflow-service
echo   API Gateway:      http://localhost:8084/gateway
echo   Monitoring:       http://localhost:8085/monitoring
echo.
echo API Documentation:
echo   User Service:     http://localhost:8081/user-service/swagger-ui.html
echo   Auth Service:     http://localhost:8082/auth-service/swagger-ui.html
echo   Workflow Service: http://localhost:8083/workflow-service/swagger-ui.html
echo.
echo Press any key to exit...
pause > NUL

ENDLOCAL
