@echo off
echo ========================================
echo   Deploying Costume Rental to Kubernetes
echo ========================================

echo.
echo Step 1: Building Docker images...
echo ========================================

echo Building user-service...
docker build -t user-service:latest -f user-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build user-service
    exit /b 1
)

echo Building costume-service...
docker build -t costume-service:latest -f costume-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build costume-service
    exit /b 1
)

echo Building bill-costume-service...
docker build -t bill-costume-service:latest -f bill-costume-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build bill-costume-service
    exit /b 1
)

echo Building supplier-service...
docker build -t supplier-service:latest -f supplier-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build supplier-service
    exit /b 1
)

echo Building import-bill-service...
docker build -t import-bill-service:latest -f import-bill-service/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build import-bill-service
    exit /b 1
)

echo Building client-costume-rental...
docker build -t client-costume-rental:latest -f client-costume-rental/Dockerfile .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build client-costume-rental
    exit /b 1
)

echo.
echo Step 2: Preparing MySQL init directory...
echo ========================================
if not exist "C:\tmp\mysql-init" mkdir "C:\tmp\mysql-init"
if exist "docker\mysql\init\*" (
    copy "docker\mysql\init\*" "C:\tmp\mysql-init\"
)

echo.
echo Step 3: Applying Kubernetes manifests...
echo ========================================

echo Creating namespace...
kubectl apply -f k8s/namespace.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to create namespace
    exit /b 1
)

echo Creating ConfigMap...
kubectl apply -f k8s/configmap.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to create ConfigMap
    exit /b 1
)

echo Creating Secret...
kubectl apply -f k8s/secret.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to create Secret
    exit /b 1
)

echo Creating PersistentVolume and PersistentVolumeClaim...
kubectl apply -f k8s/mysql-pv.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to create PV/PVC
    exit /b 1
)

echo Deploying MySQL...
kubectl apply -f k8s/mysql-deployment.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy MySQL
    exit /b 1
)

echo Waiting for MySQL to be ready...
kubectl wait --for=condition=ready pod -l app=mysql -n costume-rental --timeout=300s
if %errorlevel% neq 0 (
    echo ERROR: MySQL failed to start
    exit /b 1
)

echo Deploying User Service...
kubectl apply -f k8s/user-service.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy User Service
    exit /b 1
)

echo Deploying Supplier Service...
kubectl apply -f k8s/supplier-service.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy Supplier Service
    exit /b 1
)

echo Waiting for User and Supplier services to be ready...
kubectl wait --for=condition=ready pod -l app=user-service -n costume-rental --timeout=300s
kubectl wait --for=condition=ready pod -l app=supplier-service -n costume-rental --timeout=300s

echo Deploying Costume Service...
kubectl apply -f k8s/costume-service.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy Costume Service
    exit /b 1
)

echo Deploying Bill Costume Service...
kubectl apply -f k8s/bill-costume-service.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy Bill Costume Service
    exit /b 1
)

echo Waiting for Costume and Bill services to be ready...
kubectl wait --for=condition=ready pod -l app=costume-service -n costume-rental --timeout=300s
kubectl wait --for=condition=ready pod -l app=bill-costume-service -n costume-rental --timeout=300s

echo Deploying Import Bill Service...
kubectl apply -f k8s/import-bill-service.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy Import Bill Service
    exit /b 1
)

echo Waiting for Import Bill Service to be ready...
kubectl wait --for=condition=ready pod -l app=import-bill-service -n costume-rental --timeout=300s

echo Deploying Client Application...
kubectl apply -f k8s/client-costume-rental.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy Client Application
    exit /b 1
)

echo Deploying Ingress...
kubectl apply -f k8s/ingress.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy Ingress
    exit /b 1
)

echo.
echo Step 4: Checking deployment status...
echo ========================================
kubectl get pods -n costume-rental
kubectl get services -n costume-rental

echo.
echo ========================================
echo   Deployment completed successfully!
echo ========================================
echo.
echo Access the application at:
echo - LoadBalancer: kubectl get svc client-costume-rental -n costume-rental
echo - Port Forward: kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
echo.
echo To check logs: kubectl logs -f deployment/[service-name] -n costume-rental
echo To check status: kubectl get all -n costume-rental
echo.
pause
