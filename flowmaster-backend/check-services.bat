@echo off
chcp 65001 >nul
SETLOCAL

echo ========================================
echo    FlowMaster Backend Services Status
echo ========================================

REM Define service ports
SET "USER_PORT=8081"
SET "AUTH_PORT=8082"
SET "WORKFLOW_PORT=8083"
SET "GATEWAY_PORT=8084"
SET "MONITORING_PORT=8085"

REM Check port status
echo [INFO] Checking service port status...
echo.

REM Check User Service
netstat -ano | findstr ":%USER_PORT%" > NUL
IF %ERRORLEVEL% EQU 0 (
    echo [OK] User Service (Port %USER_PORT%) - Running
) ELSE (
    echo [X] User Service (Port %USER_PORT%) - Not Running
)

REM Check Auth Service
netstat -ano | findstr ":%AUTH_PORT%" > NUL
IF %ERRORLEVEL% EQU 0 (
    echo [OK] Auth Service (Port %AUTH_PORT%) - Running
) ELSE (
    echo [X] Auth Service (Port %AUTH_PORT%) - Not Running
)

REM Check Workflow Service
netstat -ano | findstr ":%WORKFLOW_PORT%" > NUL
IF %ERRORLEVEL% EQU 0 (
    echo [OK] Workflow Service (Port %WORKFLOW_PORT%) - Running
) ELSE (
    echo [X] Workflow Service (Port %WORKFLOW_PORT%) - Not Running
)

REM Check API Gateway
netstat -ano | findstr ":%GATEWAY_PORT%" > NUL
IF %ERRORLEVEL% EQU 0 (
    echo [OK] API Gateway (Port %GATEWAY_PORT%) - Running
) ELSE (
    echo [X] API Gateway (Port %GATEWAY_PORT%) - Not Running
)

REM Check Monitoring Service
netstat -ano | findstr ":%MONITORING_PORT%" > NUL
IF %ERRORLEVEL% EQU 0 (
    echo [OK] Monitoring Service (Port %MONITORING_PORT%) - Running
) ELSE (
    echo [X] Monitoring Service (Port %MONITORING_PORT%) - Not Running
)

echo.
echo ========================================
echo [INFO] Java Process Status:
echo ========================================

REM Display Java processes
tasklist /FI "IMAGENAME eq java.exe" /FO TABLE

echo.
echo ========================================
echo [INFO] Service Health Check:
echo ========================================

REM Health check
curl -s http://localhost:%USER_PORT%/user-service/actuator/health > NUL 2>&1
IF %ERRORLEVEL% EQU 0 (
    echo [OK] User Service Health Check - Normal
) ELSE (
    echo [X] User Service Health Check - Abnormal
)

curl -s http://localhost:%AUTH_PORT%/auth-service/actuator/health > NUL 2>&1
IF %ERRORLEVEL% EQU 0 (
    echo [OK] Auth Service Health Check - Normal
) ELSE (
    echo [X] Auth Service Health Check - Abnormal
)

curl -s http://localhost:%WORKFLOW_PORT%/workflow-service/actuator/health > NUL 2>&1
IF %ERRORLEVEL% EQU 0 (
    echo [OK] Workflow Service Health Check - Normal
) ELSE (
    echo [X] Workflow Service Health Check - Abnormal
)

echo.
echo ========================================
echo [INFO] Service Access URLs:
echo ========================================
echo   User Service:     http://localhost:%USER_PORT%/user-service
echo   Auth Service:     http://localhost:%AUTH_PORT%/auth-service
echo   Workflow Service: http://localhost:%WORKFLOW_PORT%/workflow-service
echo   API Gateway:      http://localhost:%GATEWAY_PORT%/gateway
echo   Monitoring:       http://localhost:%MONITORING_PORT%/monitoring
echo.
echo Press any key to exit...
pause > NUL

ENDLOCAL
