@echo off
chcp 65001 >nul
SETLOCAL

echo ========================================
echo    FlowMaster Backend Services Test
echo ========================================

echo [INFO] Testing environment...

REM Test Java
echo [INFO] Testing Java...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java test failed
    goto :error
)

echo.
echo [INFO] Testing Maven...
mvn -version
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven test failed
    goto :error
)

echo.
echo [INFO] Environment test passed!
echo [INFO] Current directory: %CD%
echo [INFO] Available services:

REM List available services
if exist "flowmaster-user" (
    echo   - flowmaster-user (User Service)
) else (
    echo   - flowmaster-user (NOT FOUND)
)

if exist "flowmaster-auth" (
    echo   - flowmaster-auth (Auth Service)
) else (
    echo   - flowmaster-auth (NOT FOUND)
)

if exist "flowmaster-workflow" (
    echo   - flowmaster-workflow (Workflow Service)
) else (
    echo   - flowmaster-workflow (NOT FOUND)
)

if exist "flowmaster-gateway" (
    echo   - flowmaster-gateway (API Gateway)
) else (
    echo   - flowmaster-gateway (NOT FOUND)
)

if exist "flowmaster-monitoring" (
    echo   - flowmaster-monitoring (Monitoring Service)
) else (
    echo   - flowmaster-monitoring (NOT FOUND)
)

echo.
echo ========================================
echo [SUCCESS] Test completed!
echo ========================================
echo.
echo Press any key to exit...
pause > NUL
goto :end

:error
echo.
echo ========================================
echo [ERROR] Test failed!
echo ========================================
echo.
echo Press any key to exit...
pause > NUL

:end
ENDLOCAL
