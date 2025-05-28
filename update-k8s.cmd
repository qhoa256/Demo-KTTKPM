@echo off
echo ========================================
echo Updating Costume Rental System in K8s
echo ========================================

echo.
echo Step 1: Building Updated Docker Images...
echo ========================================

echo Building Costume Service...
docker build -f costume-service/Dockerfile -t costume-service:latest .
if %ERRORLEVEL% neq 0 (
    echo Failed to build costume-service
    exit /b 1
)

echo Building Bill Costume Service...
docker build -f bill-costume-service/Dockerfile -t bill-costume-service:latest .
if %ERRORLEVEL% neq 0 (
    echo Failed to build bill-costume-service
    exit /b 1
)

echo Building Client Costume Rental...
docker build -f client-costume-rental/Dockerfile -t client-costume-rental:latest .
if %ERRORLEVEL% neq 0 (
    echo Failed to build client-costume-rental
    exit /b 1
)

echo.
echo Step 2: Tagging Images for K8s...
echo ========================================

docker tag costume-service:latest costume-rental/costume-service:k8s
docker tag bill-costume-service:latest costume-rental/bill-costume-service:k8s
docker tag client-costume-rental:latest costume-rental/client-costume-rental:k8s

echo.
echo Step 3: Updating K8s Deployments...
echo ========================================

echo Updating ConfigMap...
kubectl apply -f k8s/configmap.yaml

echo Restarting Costume Service...
kubectl delete deployment costume-service -n costume-rental
kubectl apply -f k8s/costume-service.yaml

echo Restarting Bill Costume Service...
kubectl delete deployment bill-costume-service -n costume-rental
kubectl apply -f k8s/bill-costume-service.yaml

echo Restarting Client Service...
kubectl delete deployment client-costume-rental -n costume-rental
kubectl apply -f k8s/client-costume-rental.yaml

echo.
echo Step 4: Waiting for deployments to be ready...
echo ========================================

echo Waiting for services to start...
timeout /t 30 /nobreak

kubectl get pods -n costume-rental

echo.
echo ========================================
echo Update completed!
echo ========================================
echo.
echo Access the updated application at:
echo - Main Application: http://localhost:8086
echo - Statistics Page: http://localhost:8086/costume-statistic
echo.
echo To check logs:
echo kubectl logs -f deployment/client-costume-rental -n costume-rental
echo kubectl logs -f deployment/costume-service -n costume-rental
echo kubectl logs -f deployment/bill-costume-service -n costume-rental
echo ========================================

pause
