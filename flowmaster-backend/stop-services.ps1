# FlowMaster Backend Services Shutdown Script

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "    FlowMaster Backend Services Shutdown" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "[INFO] Finding FlowMaster related Java processes..." -ForegroundColor Yellow

# Find Java processes
$javaProcesses = Get-Process -Name "java" -ErrorAction SilentlyContinue

if ($javaProcesses) {
    foreach ($process in $javaProcesses) {
        try {
            # Get process command line
            $commandLine = (Get-WmiObject Win32_Process -Filter "ProcessId = $($process.Id)").CommandLine
            
            # Check if command line contains FlowMaster
            if ($commandLine -and $commandLine -like "*flowmaster*") {
                Write-Host "[INFO] Found FlowMaster process PID: $($process.Id)" -ForegroundColor Yellow
                Write-Host "[INFO] Command line: $commandLine" -ForegroundColor Gray
                Write-Host "[INFO] Stopping process..." -ForegroundColor Yellow
                
                # Stop process
                Stop-Process -Id $process.Id -Force
                
                if ($?) {
                    Write-Host "[SUCCESS] Process $($process.Id) stopped" -ForegroundColor Green
                } else {
                    Write-Host "[ERROR] Failed to stop process $($process.Id)" -ForegroundColor Red
                }
            }
        } catch {
            Write-Host "[ERROR] Error processing process $($process.Id): $($_.Exception.Message)" -ForegroundColor Red
        }
    }
} else {
    Write-Host "[INFO] No Java processes found" -ForegroundColor Yellow
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "[SUCCESS] Services shutdown completed!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "`nPress any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
