# PowerShell script for deploying Costume Rental System to Kubernetes on Windows
# Author: Augment Agent
# Description: Deploys all microservices to Kubernetes cluster

param(
    [string]$Namespace = "costume-rental",
    [int]$Timeout = 120
)

# Color functions for better output
function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    } else {
        $input | Write-Output
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

function Write-Success { Write-ColorOutput Green $args }
function Write-Error { Write-ColorOutput Red $args }
function Write-Warning { Write-ColorOutput Yellow $args }
function Write-Info { Write-ColorOutput Cyan $args }

# Configuration
$NAMESPACE = $Namespace
$TIMEOUT = $Timeout

# Services configuration
$services = @(
    @{ name = "user-service"; port = 8081 },
    @{ name = "costume-service"; port = 8082 },
    @{ name = "bill-costume-service"; port = 8083 },
    @{ name = "supplier-service"; port = 8084 },
    @{ name = "import-bill-service"; port = 8085 },
    @{ name = "client-costume-rental"; port = 8080 }
)

Write-Info "🚀 Deploying Costume Rental System to Kubernetes..."
Write-Info "Starting deployment process..."
Write-Info "Namespace: $NAMESPACE"
Write-Info "Timeout: $($TIMEOUT)s"
Write-Info ""

# Function to wait for deployment
function Wait-ForDeployment {
    param([string]$ServiceName)
    
    Write-Info "⏳ Waiting for $ServiceName to be ready..."
    kubectl wait --for=condition=available deployment/$ServiceName -n $NAMESPACE --timeout="$($TIMEOUT)s"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Success "✅ $ServiceName is ready"
    } else {
        Write-Error "❌ $ServiceName failed to become ready within $TIMEOUT seconds"
        exit 1
    }
}

# Check if kubectl is available
try {
    kubectl version --client | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "kubectl not found"
    }
} catch {
    Write-Error "❌ kubectl is not installed or not in PATH"
    exit 1
}

# Check if cluster is accessible
try {
    kubectl cluster-info | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "Cluster not accessible"
    }
} catch {
    Write-Error "❌ Cannot connect to Kubernetes cluster. Please check your cluster connection."
    exit 1
}

# Step 1: Create namespace
Write-Info "📝 Creating namespace..."
@"
apiVersion: v1
kind: Namespace
metadata:
  name: $NAMESPACE
  labels:
    name: $NAMESPACE
"@ | kubectl apply -f -

# Step 2: Create ConfigMap
Write-Info "🔧 Deploying ConfigMaps..."
@"
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
  namespace: $NAMESPACE
data:
  environment: "kubernetes"
  log.level: "INFO"
"@ | kubectl apply -f -

# Step 3: Deploy services
Write-Info "🔧 Deploying microservices..."

foreach ($service in $services) {
    $serviceName = $service.name
    $port = $service.port
    
    Write-Info "📦 Deploying $serviceName..."
    
    # Create deployment and service
    @"
apiVersion: apps/v1
kind: Deployment
metadata:
  name: $serviceName
  namespace: $NAMESPACE
  labels:
    app: $serviceName
spec:
  replicas: 1
  selector:
    matchLabels:
      app: $serviceName
  template:
    metadata:
      labels:
        app: $serviceName
    spec:
      containers:
      - name: $serviceName
        image: costume-rental/$serviceName`:latest
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: $port
          name: http
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "kubernetes"
        resources:
          requests:
            memory: "256Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /
            port: $port
          initialDelaySeconds: 60
          periodSeconds: 30
          timeoutSeconds: 5
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /
            port: $port
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
---
apiVersion: v1
kind: Service
metadata:
  name: $serviceName
  namespace: $NAMESPACE
  labels:
    app: $serviceName
spec:
  selector:
    app: $serviceName
  ports:
  - name: http
    port: $port
    targetPort: $port
    protocol: TCP
  type: ClusterIP
"@ | kubectl apply -f -

    # Wait a bit between deployments
    Start-Sleep -Seconds 5
    Wait-ForDeployment -ServiceName $serviceName
    Write-Info ""
}

# Step 4: Deploy Ingress
Write-Info "🌐 Deploying ingress..."
@"
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: costume-rental-ingress
  namespace: $NAMESPACE
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "false"
    nginx.ingress.kubernetes.io/use-regex: "true"
    nginx.ingress.kubernetes.io/rewrite-target: /api/`$2
spec:
  ingressClassName: nginx
  rules:
  - host: costume-rental.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: client-costume-rental
            port:
              number: 8080
      - path: /api/users(/|`$)(.*)
        pathType: ImplementationSpecific
        backend:
          service:
            name: user-service
            port:
              number: 8081
      - path: /api/costumes(/|`$)(.*)
        pathType: ImplementationSpecific
        backend:
          service:
            name: costume-service
            port:
              number: 8082
      - path: /api/bills(/|`$)(.*)
        pathType: ImplementationSpecific
        backend:
          service:
            name: bill-costume-service
            port:
              number: 8083
      - path: /api/suppliers(/|`$)(.*)
        pathType: ImplementationSpecific
        backend:
          service:
            name: supplier-service
            port:
              number: 8084
      - path: /api/import-bills(/|`$)(.*)
        pathType: ImplementationSpecific
        backend:
          service:
            name: import-bill-service
            port:
              number: 8085
"@ | kubectl apply -f -

Write-Info ""
Write-Success "🎉 Deployment completed successfully!"
Write-Info ""

# Display status
Write-Info "📊 Deployment Status:"
kubectl get pods -n $NAMESPACE
Write-Info ""

Write-Info "🔗 Services:"
kubectl get svc -n $NAMESPACE
Write-Info ""

Write-Info "🌐 Ingress:"
kubectl get ingress -n $NAMESPACE
Write-Info ""

Write-Warning "📝 Next steps:"
Write-Info "1. Access the application at: http://costume-rental.local"
Write-Info "2. Test individual services:"
Write-Info "   - User Service: http://costume-rental.local/api/users"
Write-Info "   - Costume Service: http://costume-rental.local/api/costumes"
Write-Info "   - Bill Service: http://costume-rental.local/api/bills"
Write-Info "3. Monitor logs: kubectl logs -f deployment/client-costume-rental -n $NAMESPACE"
Write-Info ""

Write-Info "🔧 Useful commands:"
Write-Info "- Check pods: kubectl get pods -n $NAMESPACE"
Write-Info "- Check logs: kubectl logs -f deployment/<service-name> -n $NAMESPACE"
Write-Info "- Port forward: kubectl port-forward svc/client-costume-rental 8080:8080 -n $NAMESPACE"
Write-Info "- Delete deployment: kubectl delete namespace $NAMESPACE"
