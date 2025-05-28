@echo off
echo ========================================
echo   Kubernetes Services Logs
echo ========================================

echo.
echo Choose a service to view logs:
echo 1. User Service
echo 2. Costume Service
echo 3. Bill Costume Service
echo 4. Supplier Service
echo 5. Import Bill Service
echo 6. Client Application
echo 7. MySQL Database
echo 8. All services (last 10 lines each)
echo 9. Follow logs (real-time)
echo.

set /p choice="Enter your choice (1-9): "

if "%choice%"=="1" (
    echo Showing User Service logs...
    kubectl logs deployment/user-service -n costume-rental --tail=50
) else if "%choice%"=="2" (
    echo Showing Costume Service logs...
    kubectl logs deployment/costume-service -n costume-rental --tail=50
) else if "%choice%"=="3" (
    echo Showing Bill Costume Service logs...
    kubectl logs deployment/bill-costume-service -n costume-rental --tail=50
) else if "%choice%"=="4" (
    echo Showing Supplier Service logs...
    kubectl logs deployment/supplier-service -n costume-rental --tail=50
) else if "%choice%"=="5" (
    echo Showing Import Bill Service logs...
    kubectl logs deployment/import-bill-service -n costume-rental --tail=50
) else if "%choice%"=="6" (
    echo Showing Client Application logs...
    kubectl logs deployment/client-costume-rental -n costume-rental --tail=50
) else if "%choice%"=="7" (
    echo Showing MySQL Database logs...
    kubectl logs deployment/mysql-deployment -n costume-rental --tail=50
) else if "%choice%"=="8" (
    echo Showing all services logs (last 10 lines each)...
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
    echo.
    echo === MySQL Database ===
    kubectl logs deployment/mysql-deployment -n costume-rental --tail=10
) else if "%choice%"=="9" (
    echo Choose service to follow:
    echo 1. User Service
    echo 2. Costume Service
    echo 3. Bill Costume Service
    echo 4. Supplier Service
    echo 5. Import Bill Service
    echo 6. Client Application
    echo 7. MySQL Database
    echo.
    set /p follow_choice="Enter choice: "
    
    if "%follow_choice%"=="1" (
        echo Following User Service logs... (Press Ctrl+C to stop)
        kubectl logs deployment/user-service -n costume-rental -f
    ) else if "%follow_choice%"=="2" (
        echo Following Costume Service logs... (Press Ctrl+C to stop)
        kubectl logs deployment/costume-service -n costume-rental -f
    ) else if "%follow_choice%"=="3" (
        echo Following Bill Costume Service logs... (Press Ctrl+C to stop)
        kubectl logs deployment/bill-costume-service -n costume-rental -f
    ) else if "%follow_choice%"=="4" (
        echo Following Supplier Service logs... (Press Ctrl+C to stop)
        kubectl logs deployment/supplier-service -n costume-rental -f
    ) else if "%follow_choice%"=="5" (
        echo Following Import Bill Service logs... (Press Ctrl+C to stop)
        kubectl logs deployment/import-bill-service -n costume-rental -f
    ) else if "%follow_choice%"=="6" (
        echo Following Client Application logs... (Press Ctrl+C to stop)
        kubectl logs deployment/client-costume-rental -n costume-rental -f
    ) else if "%follow_choice%"=="7" (
        echo Following MySQL Database logs... (Press Ctrl+C to stop)
        kubectl logs deployment/mysql-deployment -n costume-rental -f
    ) else (
        echo Invalid choice!
    )
) else (
    echo Invalid choice!
)

echo.
echo ========================================
echo   Logs Operation Completed!
echo ========================================

echo.
pause
