@echo off
:menu
cls
echo ========================================
echo   Docker + K8s Management Menu
echo ========================================
echo.
echo Docker Operations:
echo 1. Build Docker Images
echo 2. Cleanup Docker Images
echo.
echo Kubernetes Operations:
echo 3. Deploy to K8s (Full Deploy)
echo 4. Start K8s Services
echo 5. Stop K8s Services
echo 6. Restart K8s Services
echo 7. Check K8s Status
echo 8. View K8s Logs
echo.
echo Combined Operations:
echo 9. Rebuild and Restart (Complete)
echo.
echo Utilities:
echo 10. Port Forward to Local
echo 11. Scale Services
echo 12. Update K8s Deployments
echo.
echo 0. Exit
echo.
echo ========================================
set /p choice="Select an option (0-12): "

if "%choice%"=="1" goto build_docker
if "%choice%"=="2" goto cleanup_docker
if "%choice%"=="3" goto deploy_k8s
if "%choice%"=="4" goto start_k8s
if "%choice%"=="5" goto stop_k8s
if "%choice%"=="6" goto restart_k8s
if "%choice%"=="7" goto status_k8s
if "%choice%"=="8" goto logs_k8s
if "%choice%"=="9" goto rebuild_k8s
if "%choice%"=="10" goto port_forward
if "%choice%"=="11" goto scale_k8s
if "%choice%"=="12" goto update_k8s
if "%choice%"=="0" goto exit

echo Invalid choice. Please try again.
pause
goto menu

:build_docker
echo.
echo Building Docker Images...
call build-docker.cmd
pause
goto menu

:cleanup_docker
echo.
echo Cleaning up Docker Images...
call cleanup-docker.cmd
pause
goto menu

:deploy_k8s
echo.
echo Deploying to Kubernetes...
call deploy-k8s.cmd
pause
goto menu

:start_k8s
echo.
echo Starting Kubernetes Services...
call start-k8s.cmd
pause
goto menu

:stop_k8s
echo.
echo Stopping Kubernetes Services...
call stop-k8s.cmd
pause
goto menu

:restart_k8s
echo.
echo Restarting Kubernetes Services...
call restart-k8s.cmd
pause
goto menu

:status_k8s
echo.
echo Checking Kubernetes Status...
call status-k8s.cmd
pause
goto menu

:logs_k8s
echo.
echo Viewing Kubernetes Logs...
call logs-k8s.cmd
pause
goto menu

:rebuild_k8s
echo.
echo Rebuilding and Restarting Complete System...
call rebuild-k8s.cmd
pause
goto menu

:port_forward
echo.
echo Setting up Port Forward...
call port-forward-k8s.cmd
pause
goto menu

:scale_k8s
echo.
echo Scaling Services...
call scale-k8s.cmd
pause
goto menu

:update_k8s
echo.
echo Updating Kubernetes Deployments...
call update-k8s.cmd
pause
goto menu

:exit
echo.
echo Goodbye!
exit /b 0
