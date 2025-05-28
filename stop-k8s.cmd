@echo off
echo ========================================
echo   Stopping Kubernetes Services
echo ========================================

echo.
echo Checking current status...
kubectl get deployments -n costume-rental 2>nul
if %errorlevel% neq 0 (
    echo No services are currently running.
    goto end
)

echo.
echo Stopping all services (scaling to 0 replicas)...

echo Stopping Client Application...
kubectl scale deployment client-costume-rental --replicas=0 -n costume-rental

echo Stopping Import Bill Service...
kubectl scale deployment import-bill-service --replicas=0 -n costume-rental

echo Stopping Bill Costume Service...
kubectl scale deployment bill-costume-service --replicas=0 -n costume-rental

echo Stopping Costume Service...
kubectl scale deployment costume-service --replicas=0 -n costume-rental

echo Stopping Supplier Service...
kubectl scale deployment supplier-service --replicas=0 -n costume-rental

echo Stopping User Service...
kubectl scale deployment user-service --replicas=0 -n costume-rental

echo Stopping MySQL...
kubectl scale deployment mysql-deployment --replicas=0 -n costume-rental

echo.
echo Waiting for all pods to terminate...
timeout /t 15 /nobreak >nul

echo.
echo Verifying shutdown...
kubectl get deployments -n costume-rental

echo.
echo Current pod status:
kubectl get pods -n costume-rental

echo.
echo ========================================
echo   All Services Stopped Successfully!
echo ========================================

echo.
echo Note:
echo - All data is preserved in PersistentVolumes
echo - To start services again, run: start-k8s.cmd
echo - To completely remove everything, run: cleanup-k8s.cmd

:end
echo.
pause
