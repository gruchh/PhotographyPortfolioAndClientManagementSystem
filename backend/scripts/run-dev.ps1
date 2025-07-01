$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir
Push-Location $projectRoot

try {
    if (-not (Test-Path "pom.xml")) {
        Write-Host "❌ pom.xml not found. Make sure you're in the project root directory." -ForegroundColor Red
        Write-Host "Current directory: $(Get-Location)" -ForegroundColor Yellow
        exit 1
    }
    if (-not (Test-Path "mvnw.cmd")) {
        Write-Host "❌ mvnw.cmd not found. Using system maven instead." -ForegroundColor Yellow
        $mavenCommand = "mvn"
    } else {
        $mavenCommand = ".\mvnw.cmd"
    }

    Write-Host "🚀 Starting Financial Transactions Monitor in DEVELOPMENT mode..." -ForegroundColor Green
    Write-Host "Using H2 in-memory database with debug logging" -ForegroundColor Cyan
    Write-Host "Application will be available at: http://localhost:8080" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Starting with command: $mavenCommand spring-boot:run -Dspring-boot.run.profiles=dev" -ForegroundColor Cyan
    Write-Host ""
    try {
        & $mavenCommand spring-boot:run "-Dspring-boot.run.profiles=dev"
    } catch {
        Write-Host "❌ Error starting application: $_" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Error: $_" -ForegroundColor Red
    exit 1
} finally {
    Pop-Location
}