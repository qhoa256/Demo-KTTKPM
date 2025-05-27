# PowerShell script to deploy Costume Rental System using Docker Compose
# Author: Augment Agent
# Description: Deploys all microservices using docker-compose

param(
    [string]$Environment = "prod",
    [switch]$Build = $false,
    [switch]$Detached = $true
)

# Color functions for better output
function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    } else {
        $input | Write-Output
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

function Write-Success { Write-ColorOutput Green $args }
function Write-Error { Write-ColorOutput Red $args }
function Write-Warning { Write-ColorOutput Yellow $args }
function Write-Info { Write-ColorOutput Cyan $args }

# Check if Docker is running
try {
    docker version | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Docker not running"
    }
} catch {
    Write-Error "❌ Docker is not running. Please start Docker Desktop."
    exit 1
}

# Change to project root directory
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

# Determine compose file
$composeFile = "docker-compose.yml"
if ($Environment -eq "dev") {
    $composeFile = "docker-compose.dev.yml"
}

Write-Info "🚀 Deploying Costume Rental System..."
Write-Info "Environment: $Environment"
Write-Info "Compose file: $composeFile"
Write-Info ""

# Build images if requested
if ($Build) {
    Write-Info "🔨 Building images first..."
    & docker-compose -f $composeFile build
    if ($LASTEXITCODE -ne 0) {
        Write-Error "❌ Failed to build images"
        exit 1
    }
    Write-Success "✅ Images built successfully"
    Write-Info ""
}

# Deploy services
Write-Info "🚀 Starting services..."
$deployArgs = @("up")
if ($Detached) {
    $deployArgs += "-d"
}

& docker-compose -f $composeFile @deployArgs

if ($LASTEXITCODE -eq 0) {
    Write-Success "🎉 Costume Rental System deployed successfully!"
    Write-Info ""
    Write-Info "📋 Service URLs:"
    Write-Info "  - Frontend: http://localhost:8080"
    Write-Info "  - User Service: http://localhost:8081"
    Write-Info "  - Costume Service: http://localhost:8082"
    Write-Info "  - Bill Service: http://localhost:8083"
    Write-Info "  - Supplier Service: http://localhost:8084"
    Write-Info "  - Import Bill Service: http://localhost:8085"
    if ($Environment -eq "dev") {
        Write-Info "  - phpMyAdmin: http://localhost:8090"
    }
    Write-Info ""
    Write-Info "🔍 Check status:"
    Write-Info "docker-compose -f $composeFile ps"
    Write-Info ""
    Write-Info "📊 View logs:"
    Write-Info "docker-compose -f $composeFile logs -f [service-name]"
} else {
    Write-Error "❌ Failed to deploy services"
    exit 1
}
