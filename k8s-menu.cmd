@echo off
:menu
cls
echo ========================================
echo   Kubernetes Management Menu
echo ========================================
echo.
echo Current Status:
kubectl get pods -n costume-rental 2>nul | find /c "Running" >nul
if %errorlevel% equ 0 (
    for /f %%i in ('kubectl get pods -n costume-rental 2^>nul ^| find /c "Running"') do set running=%%i
    echo Services Running: %running%/7
) else (
    echo Services Running: 0/7 (or not deployed)
)

echo.
echo ========================================
echo   Available Actions:
echo ========================================
echo.
echo 1. 🚀 Start Services (start-k8s.cmd)
echo 2. ⏹️  Stop Services (stop-k8s.cmd)
echo 3. 🔄 Restart Services (restart-k8s.cmd)
echo 4. 📊 Check Status (check-k8s-status.cmd)
echo 5. 📋 View Logs (view-logs-k8s.cmd)
echo 6. ⚖️  Scale Services (scale-k8s.cmd)
echo 7. 🌐 Open Application (http://localhost:8080)
echo 8. 🔧 Port Forward (port-forward-k8s.cmd)
echo 9. 🏗️  Full Deploy (deploy-k8s.cmd)
echo 10. 🗑️ Cleanup All (cleanup-k8s.cmd)
echo 0. ❌ Exit
echo.

set /p choice="Enter your choice (0-10): "

if "%choice%"=="1" (
    call start-k8s.cmd
    goto menu
) else if "%choice%"=="2" (
    call stop-k8s.cmd
    goto menu
) else if "%choice%"=="3" (
    call restart-k8s.cmd
    goto menu
) else if "%choice%"=="4" (
    call check-k8s-status.cmd
    goto menu
) else if "%choice%"=="5" (
    call view-logs-k8s.cmd
    goto menu
) else if "%choice%"=="6" (
    call scale-k8s.cmd
    goto menu
) else if "%choice%"=="7" (
    echo Opening application in browser...
    start http://localhost:8080
    goto menu
) else if "%choice%"=="8" (
    call port-forward-k8s.cmd
    goto menu
) else if "%choice%"=="9" (
    call deploy-k8s.cmd
    goto menu
) else if "%choice%"=="10" (
    call cleanup-k8s.cmd
    goto menu
) else if "%choice%"=="0" (
    echo Goodbye!
    exit /b 0
) else (
    echo Invalid choice! Please try again.
    timeout /t 2 /nobreak >nul
    goto menu
)
