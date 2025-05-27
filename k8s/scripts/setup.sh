#!/bin/bash

# Setup script for Costume Rental System Kubernetes deployment
set -e

echo "🔧 Setting up Costume Rental System for Kubernetes deployment..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Make all scripts executable
echo -e "${BLUE}📝 Making scripts executable...${NC}"
chmod +x *.sh

# Check prerequisites
echo -e "${BLUE}🔍 Checking prerequisites...${NC}"

# Check if kubectl is installed
if ! command -v kubectl &> /dev/null; then
    echo -e "${RED}❌ kubectl is not installed${NC}"
    echo "Please install kubectl: https://kubernetes.io/docs/tasks/tools/"
    exit 1
else
    echo -e "${GREEN}✅ kubectl is installed${NC}"
fi

# Check if docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker is not installed${NC}"
    echo "Please install Docker: https://docs.docker.com/get-docker/"
    exit 1
else
    echo -e "${GREEN}✅ Docker is installed${NC}"
fi

# Check if maven is installed
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven is not installed${NC}"
    echo "Please install Maven: https://maven.apache.org/install.html"
    exit 1
else
    echo -e "${GREEN}✅ Maven is installed${NC}"
fi

# Check Kubernetes cluster connection
echo -e "${BLUE}🔗 Checking Kubernetes cluster connection...${NC}"
if kubectl cluster-info &> /dev/null; then
    echo -e "${GREEN}✅ Connected to Kubernetes cluster${NC}"
    kubectl cluster-info
else
    echo -e "${RED}❌ Cannot connect to Kubernetes cluster${NC}"
    echo "Please ensure your Kubernetes cluster is running and kubectl is configured"
    exit 1
fi

# Check if ingress controller is available
echo -e "${BLUE}🌐 Checking for Ingress controller...${NC}"
if kubectl get ingressclass nginx &> /dev/null; then
    echo -e "${GREEN}✅ NGINX Ingress controller found${NC}"
elif kubectl get ingressclass &> /dev/null; then
    echo -e "${YELLOW}⚠️ Ingress controller found but not NGINX${NC}"
    echo "You may need to modify the ingress configuration"
else
    echo -e "${YELLOW}⚠️ No Ingress controller found${NC}"
    echo "Consider installing NGINX Ingress controller:"
    echo "kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml"
fi

# Add hostname to /etc/hosts if not present
echo -e "${BLUE}🌐 Checking /etc/hosts configuration...${NC}"
if ! grep -q "costume-rental.local" /etc/hosts 2>/dev/null; then
    echo -e "${YELLOW}⚠️ costume-rental.local not found in /etc/hosts${NC}"
    echo "To access the application via ingress, add this line to /etc/hosts:"
    echo "127.0.0.1 costume-rental.local"
    echo ""
    echo "Run this command:"
    echo "echo '127.0.0.1 costume-rental.local' | sudo tee -a /etc/hosts"
else
    echo -e "${GREEN}✅ costume-rental.local found in /etc/hosts${NC}"
fi

echo ""
echo -e "${GREEN}🎉 Setup completed successfully!${NC}"
echo ""
echo -e "${BLUE}📋 Next steps:${NC}"
echo "1. Build Docker images: ./build-all.sh"
echo "2. Deploy to Kubernetes: ./deploy-all.sh"
echo "3. Check status: ./check-status.sh"
echo "4. Access application: http://costume-rental.local"
echo ""
echo -e "${BLUE}🔧 Available scripts:${NC}"
echo "- ./build-all.sh     - Build all Docker images"
echo "- ./deploy-all.sh    - Deploy to Kubernetes"
echo "- ./check-status.sh  - Check deployment status"
echo "- ./cleanup.sh       - Remove all resources"
echo ""
echo -e "${BLUE}📖 For detailed instructions, see:${NC}"
echo "- k8s/README.md"
