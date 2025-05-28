@echo off
echo ========================================
echo   Restarting Kubernetes Services
echo ========================================

echo.
echo This will stop and start all services in sequence.
echo Press any key to continue or Ctrl+C to cancel...
pause >nul

echo.
echo Step 1: Stopping all services...
echo ========================================

echo Checking current status...
kubectl get deployments -n costume-rental 2>nul
if %errorlevel% neq 0 (
    echo No services are currently running.
    goto start_services
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
timeout /t 20 /nobreak >nul

:start_services
echo.
echo Step 2: Starting all services...
echo ========================================

echo Scaling MySQL to 1 replica...
kubectl scale deployment mysql-deployment --replicas=1 -n costume-rental

echo Scaling User Service to 1 replica...
kubectl scale deployment user-service --replicas=1 -n costume-rental

echo Scaling Supplier Service to 1 replica...
kubectl scale deployment supplier-service --replicas=1 -n costume-rental

echo Scaling Costume Service to 1 replica...
kubectl scale deployment costume-service --replicas=1 -n costume-rental

echo Scaling Bill Costume Service to 1 replica...
kubectl scale deployment bill-costume-service --replicas=1 -n costume-rental

echo Scaling Import Bill Service to 1 replica...
kubectl scale deployment import-bill-service --replicas=1 -n costume-rental

echo Scaling Client Application to 1 replica...
kubectl scale deployment client-costume-rental --replicas=1 -n costume-rental

echo.
echo Waiting for MySQL to be ready...
kubectl wait --for=condition=ready pod -l app=mysql-deployment -n costume-rental --timeout=300s
if %errorlevel% neq 0 (
    echo WARNING: MySQL took longer than expected to start
)

echo.
echo Waiting for core services to be ready...
kubectl wait --for=condition=available deployment/user-service -n costume-rental --timeout=300s
kubectl wait --for=condition=available deployment/costume-service -n costume-rental --timeout=300s
kubectl wait --for=condition=available deployment/bill-costume-service -n costume-rental --timeout=300s
kubectl wait --for=condition=available deployment/supplier-service -n costume-rental --timeout=300s
kubectl wait --for=condition=available deployment/import-bill-service -n costume-rental --timeout=300s

echo.
echo Waiting for client service to be ready...
kubectl wait --for=condition=available deployment/client-costume-rental -n costume-rental --timeout=600s
if %errorlevel% neq 0 (
    echo WARNING: Client service took longer than expected to start
)

echo.
echo ========================================
echo   Restart Completed Successfully!
echo ========================================

echo.
echo Current status:
kubectl get pods -n costume-rental

echo.
echo Services:
kubectl get services -n costume-rental

echo.
echo ========================================
echo   Access your application at:
echo   Main Application: http://localhost:8086
echo   Statistics Page: http://localhost:8086/costume-statistic
echo ========================================

echo.
pause
