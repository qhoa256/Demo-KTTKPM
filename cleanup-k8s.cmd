@echo off
echo ========================================
echo   Cleaning up Kubernetes deployment
echo ========================================

echo.
echo WARNING: This will permanently delete all resources!
echo This includes:
echo - All deployments and pods
echo - All services and ingress
echo - All persistent volumes and data
echo - All configmaps and secrets
echo - The entire costume-rental namespace
echo.
echo Press Ctrl+C to cancel or any key to continue...
pause >nul

echo.
echo Checking current status...
kubectl get all -n costume-rental 2>nul
if %errorlevel% neq 0 (
    echo No resources found in costume-rental namespace.
    goto cleanup_pv
)

echo.
echo Deleting all resources in costume-rental namespace...
kubectl delete namespace costume-rental

echo.
echo Waiting for namespace deletion to complete...
:wait_namespace
kubectl get namespace costume-rental >nul 2>&1
if %errorlevel% equ 0 (
    echo Still waiting for namespace deletion...
    timeout /t 5 /nobreak >nul
    goto wait_namespace
)

:cleanup_pv

echo.
echo Cleaning up PersistentVolumes...
kubectl get pv | findstr mysql-pv >nul 2>&1
if %errorlevel% equ 0 (
    kubectl delete pv mysql-pv
    echo MySQL PersistentVolume deleted.
) else (
    echo No MySQL PersistentVolume found.
)

echo.
echo Cleaning up local directories...
if exist "C:\tmp\mysql-data" (
    echo Removing C:\tmp\mysql-data...
    rmdir /s /q "C:\tmp\mysql-data"
)
if exist "C:\tmp\mysql-init" (
    echo Removing C:\tmp\mysql-init...
    rmdir /s /q "C:\tmp\mysql-init"
)

echo.
echo Cleaning up Docker images (optional)...
echo Do you want to remove Docker images as well? (y/n)
set /p remove_images="Enter choice: "
if /i "%remove_images%"=="y" (
    echo Removing Docker images...
    docker rmi costume-rental/costume-service:k8s 2>nul
    docker rmi costume-rental/bill-costume-service:k8s 2>nul
    docker rmi costume-rental/client-costume-rental:k8s 2>nul
    docker rmi costume-service:latest 2>nul
    docker rmi bill-costume-service:latest 2>nul
    docker rmi client-costume-rental:latest 2>nul
    echo Docker images removed.
) else (
    echo Docker images preserved.
)

echo.
echo Verifying cleanup...
kubectl get all -n costume-rental 2>nul
if %errorlevel% neq 0 (
    echo Namespace successfully deleted.
) else (
    echo WARNING: Some resources may still exist.
)

echo.
echo ========================================
echo   Cleanup completed!
echo ========================================
echo.
echo To redeploy the system, run: deploy-k8s.cmd
echo.
pause
