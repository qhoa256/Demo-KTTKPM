@echo off
echo ========================================
echo   Kubernetes Services Status
echo ========================================

echo.
echo Checking namespace...
kubectl get namespace costume-rental 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Namespace 'costume-rental' not found!
    echo Run deploy-k8s.cmd to create the deployment.
    goto end
)

echo.
echo ========================================
echo   Deployments Status
echo ========================================
kubectl get deployments -n costume-rental

echo.
echo ========================================
echo   Pods Status
echo ========================================
kubectl get pods -n costume-rental -o wide

echo.
echo ========================================
echo   Services Status
echo ========================================
kubectl get services -n costume-rental

echo.
echo ========================================
echo   Persistent Volumes
echo ========================================
kubectl get pv

echo.
echo ========================================
echo   ConfigMaps and Secrets
echo ========================================
kubectl get configmaps -n costume-rental
kubectl get secrets -n costume-rental

echo.
echo ========================================
echo   Resource Usage
echo ========================================
kubectl top pods -n costume-rental 2>nul
if %errorlevel% neq 0 (
    echo Metrics server not available - resource usage not shown
)

echo.
echo ========================================
echo   Service Health Check
echo ========================================

echo Checking service endpoints...
kubectl get endpoints -n costume-rental

echo.
echo ========================================
echo   Access Information
echo ========================================
echo.
echo Application URLs:
echo - Main Application: http://localhost:8086
echo - Statistics Page: http://localhost:8086/costume-statistic
echo.
echo Internal Service URLs:
echo - User Service: http://user-service:8081
echo - Costume Service: http://costume-service:8082
echo - Bill Service: http://bill-costume-service:8083
echo - Supplier Service: http://supplier-service:8084
echo - Import Bill Service: http://import-bill-service:8085
echo - Client Service: http://client-costume-rental:8086
echo.
echo Database:
echo - MySQL: mysql-service:3306
echo.

echo ========================================
echo   Quick Actions
echo ========================================
echo.
echo Available scripts:
echo - start-k8s.cmd     : Start all services
echo - stop-k8s.cmd      : Stop all services
echo - restart-k8s.cmd   : Restart all services
echo - scale-k8s.cmd     : Scale individual services
echo - update-k8s.cmd    : Update deployments with new images
echo - cleanup-k8s.cmd   : Remove all resources
echo.

:end
echo.
pause
