@echo off
echo ========================================
echo   Kubernetes Deployment Status
echo ========================================

echo.
echo Checking namespace...
kubectl get namespace costume-rental

echo.
echo Checking pods...
kubectl get pods -n costume-rental -o wide

echo.
echo Checking services...
kubectl get services -n costume-rental

echo.
echo Checking deployments...
kubectl get deployments -n costume-rental

echo.
echo Checking ingress...
kubectl get ingress -n costume-rental

echo.
echo Checking persistent volumes...
kubectl get pv
kubectl get pvc -n costume-rental

echo.
echo Recent events...
kubectl get events -n costume-rental --sort-by='.lastTimestamp' | tail -10

echo.
echo ========================================
echo   Status check completed!
echo ========================================

echo.
echo To access the application:
echo 1. Check LoadBalancer external IP:
echo    kubectl get svc client-costume-rental -n costume-rental
echo.
echo 2. Or use port forwarding:
echo    kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
echo.
echo 3. Then open: http://localhost:8080
echo.
pause
