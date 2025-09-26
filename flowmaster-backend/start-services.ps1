# FlowMaster Backend Services Startup Script

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "    FlowMaster Backend Services Startup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Check Java environment
try {
    $javaVersion = java -version 2>&1
    Write-Host "[INFO] Java environment check passed" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Java environment not installed or not in PATH" -ForegroundColor Red
    exit 1
}

# Check Maven environment
try {
    $mavenVersion = mvn -version 2>&1
    Write-Host "[INFO] Maven environment check passed" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Maven environment not installed or not in PATH" -ForegroundColor Red
    exit 1
}

# Define service list
$services = @(
    "flowmaster-user",
    "flowmaster-auth", 
    "flowmaster-workflow",
    "flowmaster-gateway",
    "flowmaster-monitoring"
)

# Start services
foreach ($service in $services) {
    Write-Host "`n[INFO] Starting service: $service" -ForegroundColor Yellow
    Write-Host "========================================" -ForegroundColor Cyan
    
    # Check if service directory exists
    if (-not (Test-Path $service)) {
        Write-Host "[ERROR] Service directory $service does not exist" -ForegroundColor Red
        continue
    }
    
    # Enter service directory
    Push-Location $service
    
    try {
        # Compile service
        Write-Host "[INFO] Compiling service $service..." -ForegroundColor Yellow
        mvn clean compile -q
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "[ERROR] Service $service compilation failed" -ForegroundColor Red
            Pop-Location
            continue
        }
        
        # Start service
        Write-Host "[INFO] Starting service $service..." -ForegroundColor Yellow
        Start-Process -FilePath "cmd" -ArgumentList "/k", "mvn spring-boot:run" -WindowStyle Normal
        
        # Wait for service startup
        Write-Host "[INFO] Waiting for service $service to start..." -ForegroundColor Yellow
        Start-Sleep -Seconds 5
        
    } catch {
        Write-Host "[ERROR] Failed to start service $service" -ForegroundColor Red
    } finally {
        Pop-Location
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "[SUCCESS] All services started successfully!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "`nService Access URLs:" -ForegroundColor Yellow
Write-Host "User Service:     http://localhost:8081/user-service" -ForegroundColor White
Write-Host "Auth Service:     http://localhost:8082/auth-service" -ForegroundColor White
Write-Host "Workflow Service: http://localhost:8083/workflow-service" -ForegroundColor White
Write-Host "API Gateway:      http://localhost:8084/gateway" -ForegroundColor White
Write-Host "Monitoring:       http://localhost:8085/monitoring" -ForegroundColor White

Write-Host "`nAPI Documentation:" -ForegroundColor Yellow
Write-Host "User Service:     http://localhost:8081/user-service/swagger-ui.html" -ForegroundColor White
Write-Host "Auth Service:     http://localhost:8082/auth-service/swagger-ui.html" -ForegroundColor White
Write-Host "Workflow Service: http://localhost:8083/workflow-service/swagger-ui.html" -ForegroundColor White

Write-Host "`nPress any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
