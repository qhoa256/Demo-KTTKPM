#!/bin/bash

# Production deploy script for Costume Rental System
set -e

echo "🚀 Deploying Costume Rental System to Kubernetes..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
NAMESPACE="costume-rental"
TIMEOUT="120s"

# Function to wait for deployment
wait_for_deployment() {
    local deployment=$1
    echo -e "${BLUE}⏳ Waiting for $deployment to be ready...${NC}"
    kubectl wait --for=condition=available --timeout=$TIMEOUT deployment/$deployment -n $NAMESPACE
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $deployment is ready${NC}"
    else
        echo -e "${YELLOW}⚠️ $deployment may not be fully ready, but continuing...${NC}"
    fi
}

# Function to deploy component
deploy_component() {
    local component=$1
    local path=$2
    echo -e "${YELLOW}📦 Deploying $component...${NC}"
    kubectl apply -f "$path"
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $component deployed${NC}"
    else
        echo -e "${RED}❌ Failed to deploy $component${NC}"
        return 1
    fi
}

echo "Starting deployment process..."
echo "Namespace: $NAMESPACE"
echo "Timeout: $TIMEOUT"
echo ""

# Step 1: Create namespace
echo -e "${BLUE}📝 Creating namespace...${NC}"
kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -

# Step 2: Deploy ConfigMaps (without database configs)
echo -e "${BLUE}🔧 Deploying ConfigMaps...${NC}"
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
  namespace: $NAMESPACE
data:
  # Service URLs for inter-service communication
  user-service-url: "http://user-service.costume-rental.svc.cluster.local:8081"
  costume-service-url: "http://costume-service.costume-rental.svc.cluster.local:8082"
  bill-service-url: "http://bill-costume-service.costume-rental.svc.cluster.local:8083"
  supplier-service-url: "http://supplier-service.costume-rental.svc.cluster.local:8084"
  import-bill-service-url: "http://import-bill-service.costume-rental.svc.cluster.local:8085"

  # Application configuration
  spring-profiles-active: "kubernetes"
  logging-level: "INFO"
EOF

# Step 3: Deploy microservices (simplified)
echo -e "${BLUE}🔧 Deploying microservices...${NC}"

SERVICES=(
    "user-service:8081"
    "costume-service:8082"
    "bill-costume-service:8083"
    "supplier-service:8084"
    "import-bill-service:8085"
    "client-costume-rental:8080"
)

for service_port in "${SERVICES[@]}"; do
    IFS=':' read -r service port <<< "$service_port"

    echo -e "${YELLOW}📦 Deploying $service...${NC}"

    cat <<EOF | kubectl apply -f -
apiVersion: apps/v1
kind: Deployment
metadata:
  name: $service
  namespace: $NAMESPACE
  labels:
    app: $service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: $service
  template:
    metadata:
      labels:
        app: $service
    spec:
      containers:
      - name: $service
        image: costume-rental/$service:latest
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 80
          name: http
        resources:
          requests:
            memory: "128Mi"
            cpu: "50m"
          limits:
            memory: "256Mi"
            cpu: "200m"
        livenessProbe:
          httpGet:
            path: /
            port: 80
          initialDelaySeconds: 30
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /
            port: 80
          initialDelaySeconds: 10
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: $service
  namespace: $NAMESPACE
  labels:
    app: $service
spec:
  selector:
    app: $service
  ports:
  - name: http
    port: $port
    targetPort: 80
    protocol: TCP
  type: ClusterIP
EOF

    # Wait a bit between deployments
    sleep 5
    wait_for_deployment "$service"
    echo ""
done

# Step 4: Deploy Ingress
echo -e "${BLUE}🌐 Deploying ingress...${NC}"
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: costume-rental-ingress
  namespace: $NAMESPACE
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
    nginx.ingress.kubernetes.io/ssl-redirect: "false"
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
      - path: /api/users
        pathType: Prefix
        backend:
          service:
            name: user-service
            port:
              number: 8081
      - path: /api/costumes
        pathType: Prefix
        backend:
          service:
            name: costume-service
            port:
              number: 8082
      - path: /api/bills
        pathType: Prefix
        backend:
          service:
            name: bill-costume-service
            port:
              number: 8083
      - path: /api/suppliers
        pathType: Prefix
        backend:
          service:
            name: supplier-service
            port:
              number: 8084
      - path: /api/import-bills
        pathType: Prefix
        backend:
          service:
            name: import-bill-service
            port:
              number: 8085
EOF

echo ""
echo -e "${GREEN}🎉 Deployment completed successfully!${NC}"
echo ""

# Display status
echo -e "${BLUE}📊 Deployment Status:${NC}"
kubectl get pods -n $NAMESPACE
echo ""

echo -e "${BLUE}🔗 Services:${NC}"
kubectl get svc -n $NAMESPACE
echo ""

echo -e "${BLUE}🌐 Ingress:${NC}"
kubectl get ingress -n $NAMESPACE
echo ""

echo -e "${YELLOW}📝 Next steps:${NC}"
echo "1. Access the application at: http://costume-rental.local"
echo "2. Test individual services:"
echo "   - User Service: http://costume-rental.local/api/users"
echo "   - Costume Service: http://costume-rental.local/api/costumes"
echo "   - Bill Service: http://costume-rental.local/api/bills"
echo "3. Monitor logs: kubectl logs -f deployment/client-costume-rental -n $NAMESPACE"
echo ""

echo -e "${BLUE}🔧 Useful commands:${NC}"
echo "- Check pods: kubectl get pods -n $NAMESPACE"
echo "- Check logs: kubectl logs -f deployment/<service-name> -n $NAMESPACE"
echo "- Port forward: kubectl port-forward svc/client-costume-rental 8080:8080 -n $NAMESPACE"
echo "- Delete deployment: kubectl delete namespace $NAMESPACE"
