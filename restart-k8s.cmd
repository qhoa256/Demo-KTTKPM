@echo off
echo ========================================
echo   Restarting Kubernetes Services
echo ========================================

echo.
echo Step 1: Stopping all services...
call stop-k8s.cmd

echo.
echo Step 2: Starting all services...
call start-k8s.cmd

echo.
echo ========================================
echo   Restart Completed!
echo ========================================
