$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir
Push-Location $projectRoot

try {
    # Sprawdź czy jesteśmy w odpowiednim katalogu
    if (-not (Test-Path "pom.xml")) {
        Write-Host "❌ pom.xml not found. Make sure you're in the project root directory." -ForegroundColor Red
        Write-Host "Current directory: $(Get-Location)" -ForegroundColor Yellow
        exit 1
    }

    # Sprawdź czy mvnw.cmd istnieje
    if (-not (Test-Path "mvnw.cmd")) {
        Write-Host "❌ mvnw.cmd not found. Using system maven instead." -ForegroundColor Yellow
        $mavenCommand = "mvn"
    } else {
        $mavenCommand = ".\mvnw.cmd"
    }

Write-Host "🔨 Building Financial Transactions Monitor..." -ForegroundColor Green
Write-Host "Running: $mavenCommand clean package -DskipTests" -ForegroundColor Cyan
Write-Host ""

# Wykonaj build
try {
    & $mavenCommand clean package -DskipTests
    $buildResult = $LASTEXITCODE
} catch {
    Write-Host "❌ Error executing maven command: $_" -ForegroundColor Red
    exit 1
}

if ($buildResult -eq 0) {
    Write-Host ""
    Write-Host "✅ Build completed successfully!" -ForegroundColor Green
    
    # Sprawdź czy katalog target istnieje
    if (-not (Test-Path "target")) {
        Write-Host "⚠️ Target directory not found" -ForegroundColor Yellow
        exit 0
    }
    
    Write-Host "JAR file created in target/ directory" -ForegroundColor Cyan
    
    # Znajdź plik JAR
    try {
        $jarFiles = Get-ChildItem -Path "target" -Filter "*.jar" -ErrorAction SilentlyContinue | 
                   Where-Object { 
                       $_.Name -like "*FinancialTransactionsMonitor*" -and 
                       $_.Name -notlike "*sources*" -and 
                       $_.Name -notlike "*javadoc*" 
                   }
        
        if ($jarFiles) {
            $jarFile = $jarFiles | Select-Object -First 1
            Write-Host "File: $($jarFile.Name)" -ForegroundColor Cyan
            Write-Host "Size: $([math]::Round($jarFile.Length / 1MB, 2)) MB" -ForegroundColor Cyan
        } else {
            Write-Host "⚠️ JAR file not found in target directory" -ForegroundColor Yellow
            # Pokaż wszystkie pliki JAR w target
            $allJars = Get-ChildItem -Path "target" -Filter "*.jar" -ErrorAction SilentlyContinue
            if ($allJars) {
                Write-Host "Available JAR files:" -ForegroundColor Cyan
                $allJars | ForEach-Object { Write-Host "  - $($_.Name)" -ForegroundColor Cyan }
            }
        }
    } catch {
        Write-Host "⚠️ Error checking JAR files: $_" -ForegroundColor Yellow
    }
    
    Write-Host ""
    Write-Host "You can now run: .\scripts\run-prod.ps1" -ForegroundColor Yellow
} else {
    Write-Host "❌ Build failed with exit code: $buildResult" -ForegroundColor Red
    exit $buildResult
}
} finally {
    # Wróć do oryginalnego katalogu
    Pop-Location
}