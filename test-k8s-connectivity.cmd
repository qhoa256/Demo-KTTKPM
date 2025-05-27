@echo off
echo ========================================
echo   Testing Kubernetes Service Connectivity
echo ========================================

echo.
echo Testing internal service connectivity...

echo.
echo 1. Testing MySQL connection from user-service...
kubectl exec deployment/user-service -n costume-rental -- curl -s mysql-service:3306 || echo "MySQL connection test completed"

echo.
echo 2. Testing bill-costume-service from client...
kubectl exec deployment/client-costume-rental -n costume-rental -- curl -s http://bill-costume-service:8083/actuator/health

echo.
echo 3. Testing user-service from client...
kubectl exec deployment/client-costume-rental -n costume-rental -- curl -s http://user-service:8081/actuator/health

echo.
echo 4. Testing costume-service from client...
kubectl exec deployment/client-costume-rental -n costume-rental -- curl -s http://costume-service:8082/actuator/health

echo.
echo 5. Testing supplier-service from client...
kubectl exec deployment/client-costume-rental -n costume-rental -- curl -s http://supplier-service:8084/actuator/health

echo.
echo 6. Testing import-bill-service from client...
kubectl exec deployment/client-costume-rental -n costume-rental -- curl -s http://import-bill-service:8085/actuator/health

echo.
echo ========================================
echo   External Access Test
echo ========================================

echo.
echo Testing external access via LoadBalancer...
curl -s http://localhost:8080/actuator/health

echo.
echo ========================================
echo   Service Endpoints Test
echo ========================================

echo.
echo Testing bill service API...
kubectl exec deployment/client-costume-rental -n costume-rental -- curl -s http://bill-costume-service:8083/api/bills

echo.
echo ========================================
echo   Connectivity Test Completed!
echo ========================================

pause
