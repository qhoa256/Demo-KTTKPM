@echo off
echo ========================================
echo   Building Docker Images for K8s
echo ========================================

echo.
echo Checking Docker status...
echo ========================================
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not running or not installed
    echo Please start Docker Desktop and try again
    exit /b 1
)

echo.
echo Cleaning up old images...
echo ========================================
docker image prune -f

echo.
echo Building all microservices...
echo ========================================

echo Building user-service...
docker build -t costume-rental/user-service:k8s -f user-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build user-service
    exit /b 1
)
echo ✓ user-service built successfully

echo.
echo Building costume-service...
docker build -t costume-rental/costume-service:k8s -f costume-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build costume-service
    exit /b 1
)
echo ✓ costume-service built successfully

echo.
echo Building bill-costume-service...
docker build -t costume-rental/bill-costume-service:k8s -f bill-costume-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build bill-costume-service
    exit /b 1
)
echo ✓ bill-costume-service built successfully

echo.
echo Building supplier-service...
docker build -t costume-rental/supplier-service:k8s -f supplier-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build supplier-service
    exit /b 1
)
echo ✓ supplier-service built successfully

echo.
echo Building import-bill-service...
docker build -t costume-rental/import-bill-service:k8s -f import-bill-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build import-bill-service
    exit /b 1
)
echo ✓ import-bill-service built successfully

echo.
echo Building client-costume-rental...
docker build -t costume-rental/client-costume-rental:k8s -f client-costume-rental/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build client-costume-rental
    exit /b 1
)
echo ✓ client-costume-rental built successfully

echo.
echo ========================================
echo   All Docker images built successfully!
echo ========================================

echo.
echo Listing built images with sizes:
docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}" | findstr -E "(REPOSITORY|costume-rental)"

echo.
echo Total disk usage by costume-rental images:
for /f "skip=1 tokens=3" %%a in ('docker images --format "{{.Size}}" costume-rental/*') do echo %%a

echo.
echo ========================================
echo   Ready for Kubernetes deployment!
echo ========================================
echo.
echo Next steps:
echo 1. Run 'deploy-k8s.cmd' to deploy to Kubernetes
echo 2. Run 'start-k8s.cmd' to start services
echo 3. Run 'rebuild-k8s.cmd' for complete rebuild and restart
echo.

REM Only pause if not called from another script
if "%1"=="" pause

REM Exit with success code
exit /b 0
