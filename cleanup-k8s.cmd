@echo off
echo ========================================
echo   Cleaning up Kubernetes deployment
echo ========================================

echo.
echo Deleting all resources in costume-rental namespace...
kubectl delete namespace costume-rental

echo.
echo Cleaning up PersistentVolumes...
kubectl delete pv mysql-pv

echo.
echo Cleaning up local directories...
if exist "C:\tmp\mysql-data" rmdir /s /q "C:\tmp\mysql-data"
if exist "C:\tmp\mysql-init" rmdir /s /q "C:\tmp\mysql-init"

echo.
echo ========================================
echo   Cleanup completed!
echo ========================================
pause
