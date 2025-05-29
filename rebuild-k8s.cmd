@echo off
echo ========================================
echo   Rebuild and Restart K8s System
echo ========================================

echo.
echo Step 1: Stopping current services...
echo ========================================
call stop-k8s.cmd

echo.
echo Step 2: Cleaning up old Docker images...
echo ========================================
call cleanup-docker.cmd

echo.
echo Step 3: Building new Docker images...
echo ========================================
call build-docker.cmd no-pause
if %errorlevel% neq 0 (
    echo ERROR: Failed to build Docker images
    exit /b 1
)

echo.
echo Step 4: Deploying to Kubernetes...
echo ========================================
call deploy-k8s.cmd
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy to Kubernetes
    exit /b 1
)

echo.
echo ========================================
echo   Rebuild and Restart completed!
echo ========================================
echo.
echo Access your application at:
echo Main Application: http://localhost:8086
echo Statistics Page: http://localhost:8086/costume-statistic
echo.
pause
