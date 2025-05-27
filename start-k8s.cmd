@echo off
echo ========================================
echo   Starting Kubernetes Services
echo ========================================

echo.
echo Checking if services are already running...
kubectl get pods -n costume-rental >nul 2>&1
if %errorlevel% neq 0 (
    echo Namespace not found. Running full deployment...
    call deploy-k8s.cmd
    goto end
)

echo.
echo Checking current status...
kubectl get deployments -n costume-rental

echo.
echo Starting all services...

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
echo Waiting for services to start...
echo This may take a few minutes...

echo.
echo Waiting for MySQL to be ready...
kubectl wait --for=condition=ready pod -l app=mysql -n costume-rental --timeout=300s
if %errorlevel% neq 0 (
    echo WARNING: MySQL took longer than expected to start
)

echo.
echo Waiting for all services to be ready...
kubectl wait --for=condition=ready pod --all -n costume-rental --timeout=300s
if %errorlevel% neq 0 (
    echo WARNING: Some services took longer than expected to start
)

echo.
echo ========================================
echo   Services Started Successfully!
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
echo   http://localhost:8080
echo ========================================

:end
echo.
pause
