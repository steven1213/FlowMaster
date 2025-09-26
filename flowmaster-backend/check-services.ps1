# FlowMaster Backend Services Management Scripts

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "    FlowMaster Backend Services" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Define service ports
$services = @{
    "User Service" = 8081
    "Auth Service" = 8082
    "Workflow Service" = 8083
    "API Gateway" = 8084
    "Monitoring Service" = 8085
}

Write-Host "`n[INFO] Checking service port status..." -ForegroundColor Yellow

foreach ($service in $services.GetEnumerator()) {
    $port = $service.Value
    $name = $service.Key
    
    $connection = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
    
    if ($connection) {
        Write-Host "[OK] $name (Port $port) - Running" -ForegroundColor Green
    } else {
        Write-Host "[X] $name (Port $port) - Not Running" -ForegroundColor Red
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "[INFO] Java Process Status:" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Display Java processes
Get-Process -Name "java" -ErrorAction SilentlyContinue | Format-Table ProcessName, Id, CPU, WorkingSet -AutoSize

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "[INFO] Service Health Check:" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Health check
$healthChecks = @(
    @{ Name = "User Service"; Url = "http://localhost:8081/user-service/actuator/health" }
    @{ Name = "Auth Service"; Url = "http://localhost:8082/auth-service/actuator/health" }
    @{ Name = "Workflow Service"; Url = "http://localhost:8083/workflow-service/actuator/health" }
)

foreach ($check in $healthChecks) {
    try {
        $response = Invoke-WebRequest -Uri $check.Url -TimeoutSec 5 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "[OK] $($check.Name) Health Check - Normal" -ForegroundColor Green
        } else {
            Write-Host "[X] $($check.Name) Health Check - Abnormal" -ForegroundColor Red
        }
    } catch {
        Write-Host "[X] $($check.Name) Health Check - Abnormal" -ForegroundColor Red
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "[INFO] Service Access URLs:" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "User Service:     http://localhost:8081/user-service" -ForegroundColor White
Write-Host "Auth Service:     http://localhost:8082/auth-service" -ForegroundColor White
Write-Host "Workflow Service: http://localhost:8083/workflow-service" -ForegroundColor White
Write-Host "API Gateway:      http://localhost:8084/gateway" -ForegroundColor White
Write-Host "Monitoring:       http://localhost:8085/monitoring" -ForegroundColor White

Write-Host "`nPress any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
