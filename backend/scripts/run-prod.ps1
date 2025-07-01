$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir
Push-Location $projectRoot

function Load-EnvFile {
    param([string]$EnvFile = ".env")
    
    if (Test-Path $EnvFile) {
        Write-Host "📄 Loading environment variables from $EnvFile..." -ForegroundColor Cyan
        Get-Content $EnvFile | ForEach-Object {
            if ($_ -and !$_.StartsWith('#') -and $_.Contains('=')) {
                $name, $value = $_ -split '=', 2
                $name = $name.Trim()
                $value = $value.Trim()
                if ($value.StartsWith('"') -and $value.EndsWith('"')) {
                    $value = $value.Substring(1, $value.Length - 2)
                }
                [System.Environment]::SetEnvironmentVariable($name, $value, [System.EnvironmentVariableTarget]::Process)
            }
        }
        Write-Host ""
    }
}

try {
    Load-EnvFile
    Write-Host "🚀 Starting Financial Transactions Monitor in PRODUCTION mode..." -ForegroundColor Green
    Write-Host "Make sure you have built the application first (mvn clean package)" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Checking if JAR file exists..." -ForegroundColor Cyan
    
    if (-not (Test-Path "target")) {
        Write-Host "❌ Target directory not found!" -ForegroundColor Red
        Write-Host "Please build the application first using: .\scripts\build.ps1" -ForegroundColor Yellow
        exit 1
    }

    $jarFiles = Get-ChildItem -Path "target" -Filter "*.jar" -ErrorAction SilentlyContinue |
               Where-Object { 
                   $_.Name -notlike "*sources*" -and 
                   $_.Name -notlike "*javadoc*" 
               }
    
    if ($jarFiles) {
        $jarFile = $jarFiles | Select-Object -First 1
        Write-Host "✅ JAR file found: $($jarFile.Name)" -ForegroundColor Green
        Write-Host "Ensure PostgreSQL is running and environment variables are set:" -ForegroundColor Yellow
        Write-Host "  DB_NAME, DB_USERNAME, DB_PASSWORD" -ForegroundColor Yellow
        Write-Host ""
        
        # Check environment variables
        $envVars = @("DB_NAME", "DB_USERNAME", "DB_PASSWORD")
        $missingVars = @()
        
        foreach ($var in $envVars) {
            if (-not [System.Environment]::GetEnvironmentVariable($var)) {
                $missingVars += $var
            }
        }
        
        if ($missingVars.Count -gt 0) {
            Write-Host "⚠️ Warning: Missing environment variables:" -ForegroundColor Yellow
            $missingVars | ForEach-Object { Write-Host "  - $_" -ForegroundColor Yellow }
            Write-Host ""
        }
        
        Write-Host "Starting application..." -ForegroundColor Cyan
        java -jar "target/$($jarFile.Name)" --spring.profiles.active=prod
    } else {
        Write-Host "❌ No executable JAR file found!" -ForegroundColor Red
        
        $allJars = Get-ChildItem -Path "target" -Filter "*.jar" -ErrorAction SilentlyContinue
        if ($allJars) {
            Write-Host "Available JAR files in target directory:" -ForegroundColor Cyan
            $allJars | ForEach-Object { Write-Host "  - $($_.Name)" -ForegroundColor Cyan }
        } else {
            Write-Host "No JAR files found in target directory." -ForegroundColor Red
        }
        
        Write-Host "Please build the application first using: .\scripts\build.ps1" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "❌ Error starting application: $_" -ForegroundColor Red
    exit 1
} finally {
    Pop-Location
}