# PowerShell script for cleaning up Costume Rental System deployment on Windows
# Author: Augment Agent
# Description: Removes all deployed resources and Docker images

param(
    [string]$Namespace = "costume-rental",
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

$NAMESPACE = $Namespace

Write-Warning "🗑️ Costume Rental System Cleanup"
Write-Info "This will remove all deployed resources in namespace: $NAMESPACE"

if ($RemoveImages) {
    Write-Warning "⚠️ Docker images will also be removed!"
}

Write-Info ""

# Confirmation prompt
if (-not $Force) {
    $confirmation = Read-Host "Are you sure you want to proceed? (y/N)"
    if ($confirmation -ne 'y' -and $confirmation -ne 'Y') {
        Write-Info "Cleanup cancelled."
        exit 0
    }
}

Write-Info "🧹 Starting cleanup process..."
Write-Info ""

# Function to check if namespace exists
function Test-NamespaceExists {
    param([string]$NamespaceName)
    
    try {
        kubectl get namespace $NamespaceName | Out-Null
        return $LASTEXITCODE -eq 0
    }
    catch {
        return $false
    }
}

# Step 1: Remove Kubernetes resources
if (Test-NamespaceExists -NamespaceName $NAMESPACE) {
    Write-Info "📋 Current resources in namespace $NAMESPACE:"
    kubectl get all -n $NAMESPACE
    Write-Info ""
    
    Write-Info "🗑️ Deleting namespace $NAMESPACE..."
    kubectl delete namespace $NAMESPACE
    
    if ($LASTEXITCODE -eq 0) {
        Write-Success "✅ Namespace $NAMESPACE deleted successfully"
    } else {
        Write-Error "❌ Failed to delete namespace $NAMESPACE"
    }
} else {
    Write-Warning "⚠️ Namespace $NAMESPACE does not exist"
}

Write-Info ""

# Step 2: Remove Docker images if requested
if ($RemoveImages) {
    Write-Info "🐳 Removing Docker images..."
    
    # Get all costume-rental images
    $images = docker images --format "table {{.Repository}}:{{.Tag}}" | Select-String "costume-rental"
    
    if ($images) {
        Write-Info "Found costume-rental images:"
        $images | ForEach-Object { Write-Info "  - $_" }
        Write-Info ""
        
        # Remove each image
        $images | ForEach-Object {
            $imageName = $_.ToString().Trim()
            Write-Info "Removing image: $imageName"
            docker rmi $imageName
            
            if ($LASTEXITCODE -eq 0) {
                Write-Success "✅ Removed $imageName"
            } else {
                Write-Warning "⚠️ Failed to remove $imageName"
            }
        }
    } else {
        Write-Info "No costume-rental images found"
    }
    
    Write-Info ""
    Write-Info "🧹 Cleaning up unused Docker resources..."
    
    # Remove unused images
    docker image prune -f
    
    # Remove unused volumes
    docker volume prune -f
    
    # Remove unused networks
    docker network prune -f
    
    Write-Success "✅ Docker cleanup completed"
}

Write-Info ""

# Step 3: Verification
Write-Info "🔍 Verification:"

# Check if namespace still exists
if (Test-NamespaceExists -NamespaceName $NAMESPACE) {
    Write-Warning "⚠️ Namespace $NAMESPACE still exists (may be terminating)"
    Write-Info "Check status: kubectl get namespace $NAMESPACE"
} else {
    Write-Success "✅ Namespace $NAMESPACE successfully removed"
}

# Check remaining images
if ($RemoveImages) {
    $remainingImages = docker images --format "table {{.Repository}}:{{.Tag}}" | Select-String "costume-rental"
    if ($remainingImages) {
        Write-Warning "⚠️ Some costume-rental images still exist:"
        $remainingImages | ForEach-Object { Write-Info "  - $_" }
    } else {
        Write-Success "✅ All costume-rental images removed"
    }
}

Write-Info ""
Write-Success "🎉 Cleanup process completed!"
Write-Info ""

Write-Info "📝 Next steps:"
Write-Info "1. To redeploy: .\build-all.ps1 && .\deploy-simple.ps1"
Write-Info "2. To check cluster status: kubectl get all --all-namespaces"
Write-Info "3. To check Docker images: docker images"
Write-Info ""

Write-Warning "💡 Tips:"
Write-Info "- Use -RemoveImages to also clean Docker images"
Write-Info "- Use -Force to skip confirmation prompt"
Write-Info "- Example: .\cleanup.ps1 -RemoveImages -Force"
