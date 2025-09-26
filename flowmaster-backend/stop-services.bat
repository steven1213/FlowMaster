@echo off
chcp 65001 >nul
SETLOCAL

echo ========================================
echo    FlowMaster Backend Services Shutdown
echo ========================================

REM Find Java processes
echo [INFO] Finding FlowMaster related Java processes...

FOR /F "tokens=2" %%I IN ('tasklist /FI "IMAGENAME eq java.exe" /FO CSV ^| findstr "java.exe"') DO (
    SET "PID=%%I"
    SET "PID=!PID:"=!"
    
    REM Check if process command line contains FlowMaster
    FOR /F "tokens=*" %%J IN ('wmic process where "ProcessId=!PID!" get CommandLine /value ^| findstr "CommandLine"') DO (
        SET "CMD=%%J"
        SET "CMD=!CMD:CommandLine=!"
        SET "CMD=!CMD:=!"
        
        REM Check if contains FlowMaster related path
        echo !CMD! | findstr /i "flowmaster" > NUL
        IF !ERRORLEVEL! EQU 0 (
            echo [INFO] Found FlowMaster process PID: !PID!
            echo [INFO] Command line: !CMD!
            echo [INFO] Stopping process...
            taskkill /PID !PID! /F
            IF !ERRORLEVEL! EQU 0 (
                echo [SUCCESS] Process !PID! stopped
            ) ELSE (
                echo [ERROR] Failed to stop process !PID!
            )
        )
    )
)

echo.
echo ========================================
echo [SUCCESS] Services shutdown completed!
echo ========================================
echo.
echo Press any key to exit...
pause > NUL

ENDLOCAL
