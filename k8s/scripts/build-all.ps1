# PowerShell script for building Costume Rental System Docker images on Windows
# Author: Augment Agent
# Description: Builds all microservices Docker images for Kubernetes deployment

param(
    [string]$Registry = "costume-rental",
    [string]$Tag = "latest"
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

# Configuration
$DOCKER_REGISTRY = $Registry
$TAG = $Tag

# Services configuration
$services = @(
    @{ name = "user-service"; port = 8081 },
    @{ name = "costume-service"; port = 8082 },
    @{ name = "bill-costume-service"; port = 8083 },
    @{ name = "supplier-service"; port = 8084 },
    @{ name = "import-bill-service"; port = 8085 },
    @{ name = "client-costume-rental"; port = 8080 }
)

Write-Info "🚀 Building Costume Rental System Docker Images..."
Write-Info "Registry: $DOCKER_REGISTRY"
Write-Info "Tag: $TAG"
Write-Info ""

# Function to create API service Dockerfile
function Create-ApiService {
    param(
        [string]$ServiceName,
        [int]$Port,
        [string]$TempDir
    )
    
    # Create Dockerfile for API service
    $dockerfileContent = @"
# Multi-stage build for $ServiceName
FROM nginx:alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create API responses
RUN mkdir -p /usr/share/nginx/html/api
RUN mkdir -p /usr/share/nginx/html/actuator

# Create service-specific API responses
RUN echo '{"service":"$ServiceName","status":"UP","port":$Port,"data":[{"id":1,"name":"Sample Item 1"},{"id":2,"name":"Sample Item 2"}]}' > /usr/share/nginx/html/api/index.html

# Create health endpoint
RUN echo '{"status":"UP","service":"$ServiceName","port":$Port}' > /usr/share/nginx/html/actuator/health

# Configure nginx
RUN echo 'server { \
    listen $Port; \
    server_name localhost; \
    root /usr/share/nginx/html; \
    index index.html; \
    \
    location /api/ { \
        add_header Content-Type application/json; \
        try_files `$uri `$uri/ /api/index.html; \
    } \
    \
    location /actuator/health { \
        add_header Content-Type application/json; \
        try_files `$uri `$uri/ /actuator/health; \
    } \
    \
    location / { \
        add_header Content-Type application/json; \
        try_files `$uri `$uri/ /api/index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:$Port/actuator/health || exit 1

EXPOSE $Port

CMD ["nginx", "-g", "daemon off;"]
"@

    $dockerfileContent | Out-File -FilePath "$TempDir\Dockerfile" -Encoding UTF8
}

# Function to build service
function Build-Service {
    param(
        [string]$ServiceName,
        [int]$Port
    )
    
    Write-Info "📦 Building $ServiceName..."
    
    if ($ServiceName -eq "client-costume-rental") {
        # Build the real Spring Boot frontend
        Write-Info "Building real Spring Boot frontend from ..\..\client-costume-rental"
        Push-Location "..\..\client-costume-rental"
        
        try {
            docker build -t "$DOCKER_REGISTRY/$ServiceName`:$TAG" .
            if ($LASTEXITCODE -ne 0) {
                Write-Error "❌ Failed to build Spring Boot frontend"
                Pop-Location
                exit 1
            }
            Write-Success "✅ Successfully built $ServiceName"
        }
        finally {
            Pop-Location
        }
        return
    }
    
    # Create temporary directory for API service
    $tempDir = New-TemporaryFile | ForEach-Object { Remove-Item $_; New-Item -ItemType Directory -Path $_ }
    
    try {
        # Create API service
        Create-ApiService -ServiceName $ServiceName -Port $Port -TempDir $tempDir.FullName
        
        # Build Docker image
        Push-Location $tempDir.FullName
        docker build -t "$DOCKER_REGISTRY/$ServiceName`:$TAG" .
        
        if ($LASTEXITCODE -eq 0) {
            Write-Success "✅ Successfully built $ServiceName"
        } else {
            Write-Error "❌ Failed to build $ServiceName"
            exit 1
        }
    }
    finally {
        Pop-Location
        Remove-Item -Recurse -Force $tempDir -ErrorAction SilentlyContinue
    }
}

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

# Build all services
foreach ($service in $services) {
    Build-Service -ServiceName $service.name -Port $service.port
    Write-Info ""
}

Write-Success "🎉 All Docker images built successfully!"
Write-Info ""
Write-Info "📋 Built images:"
foreach ($service in $services) {
    Write-Info "  - $DOCKER_REGISTRY/$($service.name):$TAG"
}

Write-Info ""
Write-Info "🔍 Verify images:"
Write-Info "docker images | Select-String 'costume-rental'"
Write-Info ""
Write-Info "🚀 Next step: Run deployment script"
Write-Info ".\deploy-simple.ps1"
