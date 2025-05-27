#!/bin/bash

# Deploy script for Costume Rental System to Kubernetes
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
TIMEOUT="300s"

# Function to wait for deployment
wait_for_deployment() {
    local deployment=$1
    echo -e "${BLUE}⏳ Waiting for $deployment to be ready...${NC}"
    kubectl wait --for=condition=available --timeout=$TIMEOUT deployment/$deployment -n $NAMESPACE
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $deployment is ready${NC}"
    else
        echo -e "${RED}❌ $deployment failed to become ready${NC}"
        return 1
    fi
}

# Function to check if namespace exists
check_namespace() {
    if ! kubectl get namespace $NAMESPACE > /dev/null 2>&1; then
        echo -e "${YELLOW}📝 Creating namespace $NAMESPACE...${NC}"
        kubectl apply -f ../namespace/
    else
        echo -e "${GREEN}✅ Namespace $NAMESPACE already exists${NC}"
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
check_namespace

# Step 2: Deploy secrets and configmaps
echo -e "${BLUE}🔐 Deploying secrets and configmaps...${NC}"
deploy_component "Secrets" "../secrets/"
deploy_component "ConfigMaps" "../configmaps/"

# Step 3: Deploy database
echo -e "${BLUE}🗄️ Deploying database...${NC}"
deploy_component "MySQL PV" "../database/mysql-pv.yaml"
deploy_component "MySQL" "../database/mysql-deployment.yaml"

# Wait for MySQL to be ready
wait_for_deployment "mysql"

# Give MySQL some time to initialize
echo -e "${BLUE}⏳ Waiting for MySQL to initialize...${NC}"
sleep 30

# Step 4: Deploy microservices
echo -e "${BLUE}🔧 Deploying microservices...${NC}"

# Deploy in dependency order
SERVICES=(
    "user-service"
    "supplier-service"
    "costume-service"
    "bill-costume-service"
    "import-bill-service"
    "client-costume-rental"
)

for service in "${SERVICES[@]}"; do
    deploy_component "$service" "../services/$service/"
    wait_for_deployment "$service"
    echo ""
done

# Step 5: Deploy ingress
echo -e "${BLUE}🌐 Deploying ingress...${NC}"
deploy_component "Ingress" "../ingress/"

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
echo "1. Add '127.0.0.1 costume-rental.local' to your /etc/hosts file"
echo "2. Access the application at: http://costume-rental.local"
echo "3. Monitor logs: kubectl logs -f deployment/client-costume-rental -n $NAMESPACE"
echo ""

echo -e "${BLUE}🔧 Useful commands:${NC}"
echo "- Check pods: kubectl get pods -n $NAMESPACE"
echo "- Check logs: kubectl logs -f deployment/<service-name> -n $NAMESPACE"
echo "- Port forward: kubectl port-forward svc/client-costume-rental 8080:8080 -n $NAMESPACE"
echo "- Scale service: kubectl scale deployment <service-name> --replicas=3 -n $NAMESPACE"
