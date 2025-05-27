@echo off
echo ========================================
echo   Scale Kubernetes Services
echo ========================================

echo.
echo Current deployment status:
kubectl get deployments -n costume-rental

echo.
echo Choose a service to scale:
echo 1. User Service
echo 2. Costume Service
echo 3. Bill Costume Service
echo 4. Supplier Service
echo 5. Import Bill Service
echo 6. Client Application
echo 7. Scale all services to 2 replicas
echo 8. Scale all services to 1 replica
echo.

set /p choice="Enter your choice (1-8): "
set /p replicas="Enter number of replicas (1-5): "

if "%choice%"=="1" (
    echo Scaling User Service to %replicas% replicas...
    kubectl scale deployment user-service --replicas=%replicas% -n costume-rental
) else if "%choice%"=="2" (
    echo Scaling Costume Service to %replicas% replicas...
    kubectl scale deployment costume-service --replicas=%replicas% -n costume-rental
) else if "%choice%"=="3" (
    echo Scaling Bill Costume Service to %replicas% replicas...
    kubectl scale deployment bill-costume-service --replicas=%replicas% -n costume-rental
) else if "%choice%"=="4" (
    echo Scaling Supplier Service to %replicas% replicas...
    kubectl scale deployment supplier-service --replicas=%replicas% -n costume-rental
) else if "%choice%"=="5" (
    echo Scaling Import Bill Service to %replicas% replicas...
    kubectl scale deployment import-bill-service --replicas=%replicas% -n costume-rental
) else if "%choice%"=="6" (
    echo Scaling Client Application to %replicas% replicas...
    kubectl scale deployment client-costume-rental --replicas=%replicas% -n costume-rental
) else if "%choice%"=="7" (
    echo Scaling all services to 2 replicas...
    kubectl scale deployment user-service --replicas=2 -n costume-rental
    kubectl scale deployment costume-service --replicas=2 -n costume-rental
    kubectl scale deployment bill-costume-service --replicas=2 -n costume-rental
    kubectl scale deployment supplier-service --replicas=2 -n costume-rental
    kubectl scale deployment import-bill-service --replicas=2 -n costume-rental
    kubectl scale deployment client-costume-rental --replicas=2 -n costume-rental
) else if "%choice%"=="8" (
    echo Scaling all services to 1 replica...
    kubectl scale deployment user-service --replicas=1 -n costume-rental
    kubectl scale deployment costume-service --replicas=1 -n costume-rental
    kubectl scale deployment bill-costume-service --replicas=1 -n costume-rental
    kubectl scale deployment supplier-service --replicas=1 -n costume-rental
    kubectl scale deployment import-bill-service --replicas=1 -n costume-rental
    kubectl scale deployment client-costume-rental --replicas=1 -n costume-rental
) else (
    echo Invalid choice!
    goto end
)

echo.
echo Waiting for scaling to complete...
timeout /t 5 /nobreak >nul

echo.
echo Updated deployment status:
kubectl get deployments -n costume-rental

:end
echo.
pause
