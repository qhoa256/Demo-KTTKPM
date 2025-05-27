@echo off
echo ========================================
echo   Viewing Kubernetes Logs
echo ========================================

echo.
echo Choose a service to view logs:
echo 1. MySQL
echo 2. User Service
echo 3. Costume Service
echo 4. Bill Costume Service
echo 5. Supplier Service
echo 6. Import Bill Service
echo 7. Client Application
echo 8. All Services (last 10 lines each)
echo.

set /p choice="Enter your choice (1-8): "

if "%choice%"=="1" (
    echo Viewing MySQL logs...
    kubectl logs deployment/mysql-deployment -n costume-rental --tail=50
) else if "%choice%"=="2" (
    echo Viewing User Service logs...
    kubectl logs deployment/user-service -n costume-rental --tail=50
) else if "%choice%"=="3" (
    echo Viewing Costume Service logs...
    kubectl logs deployment/costume-service -n costume-rental --tail=50
) else if "%choice%"=="4" (
    echo Viewing Bill Costume Service logs...
    kubectl logs deployment/bill-costume-service -n costume-rental --tail=50
) else if "%choice%"=="5" (
    echo Viewing Supplier Service logs...
    kubectl logs deployment/supplier-service -n costume-rental --tail=50
) else if "%choice%"=="6" (
    echo Viewing Import Bill Service logs...
    kubectl logs deployment/import-bill-service -n costume-rental --tail=50
) else if "%choice%"=="7" (
    echo Viewing Client Application logs...
    kubectl logs deployment/client-costume-rental -n costume-rental --tail=50
) else if "%choice%"=="8" (
    echo Viewing all service logs...
    echo.
    echo === MySQL ===
    kubectl logs deployment/mysql-deployment -n costume-rental --tail=10
    echo.
    echo === User Service ===
    kubectl logs deployment/user-service -n costume-rental --tail=10
    echo.
    echo === Costume Service ===
    kubectl logs deployment/costume-service -n costume-rental --tail=10
    echo.
    echo === Bill Costume Service ===
    kubectl logs deployment/bill-costume-service -n costume-rental --tail=10
    echo.
    echo === Supplier Service ===
    kubectl logs deployment/supplier-service -n costume-rental --tail=10
    echo.
    echo === Import Bill Service ===
    kubectl logs deployment/import-bill-service -n costume-rental --tail=10
    echo.
    echo === Client Application ===
    kubectl logs deployment/client-costume-rental -n costume-rental --tail=10
) else (
    echo Invalid choice!
)

echo.
pause
