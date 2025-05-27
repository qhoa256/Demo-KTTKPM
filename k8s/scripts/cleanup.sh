#!/bin/bash

# Cleanup script for Costume Rental System
set -e

echo "🧹 Cleaning up Costume Rental System from Kubernetes..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
NAMESPACE="costume-rental"

# Function to delete component
delete_component() {
    local component=$1
    local path=$2
    echo -e "${YELLOW}🗑️ Deleting $component...${NC}"
    kubectl delete -f "$path" --ignore-not-found=true
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $component deleted${NC}"
    else
        echo -e "${RED}❌ Failed to delete $component${NC}"
    fi
}

echo "Starting cleanup process..."
echo "Namespace: $NAMESPACE"
echo ""

# Check if namespace exists
if ! kubectl get namespace $NAMESPACE > /dev/null 2>&1; then
    echo -e "${YELLOW}⚠️ Namespace $NAMESPACE does not exist${NC}"
    exit 0
fi

# Step 1: Delete ingress
echo -e "${BLUE}🌐 Deleting ingress...${NC}"
delete_component "Ingress" "../ingress/"

# Step 2: Delete services (in reverse order)
echo -e "${BLUE}🔧 Deleting microservices...${NC}"

SERVICES=(
    "client-costume-rental"
    "import-bill-service"
    "bill-costume-service"
    "costume-service"
    "supplier-service"
    "user-service"
)

for service in "${SERVICES[@]}"; do
    delete_component "$service" "../services/$service/"
done

# Step 3: Delete database
echo -e "${BLUE}🗄️ Deleting database...${NC}"
delete_component "MySQL" "../database/mysql-deployment.yaml"
delete_component "MySQL PV" "../database/mysql-pv.yaml"

# Step 4: Delete configmaps and secrets
echo -e "${BLUE}🔐 Deleting secrets and configmaps...${NC}"
delete_component "ConfigMaps" "../configmaps/"
delete_component "Secrets" "../secrets/"

# Step 5: Delete namespace
echo -e "${BLUE}📝 Deleting namespace...${NC}"
kubectl delete namespace $NAMESPACE --ignore-not-found=true

echo ""
echo -e "${GREEN}🎉 Cleanup completed successfully!${NC}"
echo ""

# Verify cleanup
echo -e "${BLUE}📊 Verification:${NC}"
if kubectl get namespace $NAMESPACE > /dev/null 2>&1; then
    echo -e "${YELLOW}⚠️ Namespace still exists (may take a moment to fully delete)${NC}"
    kubectl get all -n $NAMESPACE 2>/dev/null || echo "No resources found"
else
    echo -e "${GREEN}✅ Namespace successfully deleted${NC}"
fi

echo ""
echo -e "${BLUE}🔧 Additional cleanup (if needed):${NC}"
echo "- Remove Docker images: docker rmi \$(docker images costume-rental/* -q)"
echo "- Remove from /etc/hosts: sudo sed -i '/costume-rental.local/d' /etc/hosts"
