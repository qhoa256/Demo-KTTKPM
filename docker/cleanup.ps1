# PowerShell script to cleanup Costume Rental System Docker resources
# Author: Augment Agent
# Description: Stops and removes all containers, networks, and optionally volumes

param(
    [string]$Environment = "prod",
    [switch]$RemoveVolumes = $false,
    [switch]$RemoveImages = $false,
    [switch]$Force = $false
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

Write-Warning "🧹 Cleaning up Costume Rental System..."
Write-Info "Environment: $Environment"
Write-Info "Compose file: $composeFile"

if ($RemoveVolumes) {
    Write-Warning "⚠️  This will remove all data volumes!"
}
if ($RemoveImages) {
    Write-Warning "⚠️  This will remove all Docker images!"
}

if (-not $Force) {
    $confirmation = Read-Host "Are you sure you want to continue? (y/N)"
    if ($confirmation -ne "y" -and $confirmation -ne "Y") {
        Write-Info "Operation cancelled."
        exit 0
    }
}

Write-Info ""

# Stop and remove containers
Write-Info "🛑 Stopping services..."
& docker-compose -f $composeFile down

if ($LASTEXITCODE -eq 0) {
    Write-Success "✅ Services stopped successfully"
} else {
    Write-Warning "⚠️  Some services may not have been running"
}

# Remove volumes if requested
if ($RemoveVolumes) {
    Write-Info "🗑️  Removing volumes..."
    & docker-compose -f $composeFile down -v
    if ($LASTEXITCODE -eq 0) {
        Write-Success "✅ Volumes removed successfully"
    } else {
        Write-Warning "⚠️  Some volumes may not exist"
    }
}

# Remove images if requested
if ($RemoveImages) {
    Write-Info "🗑️  Removing images..."
    $services = @(
        "costume-rental/user-service",
        "costume-rental/costume-service",
        "costume-rental/bill-costume-service",
        "costume-rental/supplier-service",
        "costume-rental/import-bill-service",
        "costume-rental/client-costume-rental"
    )
    
    foreach ($service in $services) {
        Write-Info "Removing image: $service"
        & docker rmi $service -f 2>$null
    }
    Write-Success "✅ Images cleanup completed"
}

# Clean up unused resources
Write-Info "🧹 Cleaning up unused Docker resources..."
& docker system prune -f

Write-Success "🎉 Cleanup completed!"
Write-Info ""
Write-Info "🔍 Check remaining resources:"
Write-Info "docker ps -a"
Write-Info "docker images"
Write-Info "docker volume ls"
