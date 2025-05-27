#!/bin/bash

# Build script for all microservices
set -e

echo "🚀 Building all microservices for Costume Rental System..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
DOCKER_REGISTRY=${DOCKER_REGISTRY:-"costume-rental"}
TAG=${TAG:-"latest"}

# Services to build
SERVICES=(
    "user-service"
    "costume-service"
    "bill-costume-service"
    "supplier-service"
    "import-bill-service"
    "client-costume-rental"
)

# Function to build a service
build_service() {
    local service=$1
    echo -e "${YELLOW}📦 Building $service...${NC}"
    
    cd "../$service"
    
    # Build with Maven
    echo "Building JAR file..."
    mvn clean package -DskipTests -q
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Failed to build $service${NC}"
        exit 1
    fi
    
    # Build Docker image
    echo "Building Docker image..."
    docker build -t "$DOCKER_REGISTRY/$service:$TAG" .
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Failed to build Docker image for $service${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ Successfully built $service${NC}"
    cd - > /dev/null
}

# Main build process
echo "Starting build process..."
echo "Registry: $DOCKER_REGISTRY"
echo "Tag: $TAG"
echo ""

# Build parent project first
echo -e "${YELLOW}📦 Building parent project...${NC}"
cd ..
mvn clean install -DskipTests -q

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to build parent project${NC}"
    exit 1
fi

cd k8s/scripts

# Build each service
for service in "${SERVICES[@]}"; do
    build_service "$service"
    echo ""
done

echo -e "${GREEN}🎉 All services built successfully!${NC}"
echo ""
echo "Built images:"
for service in "${SERVICES[@]}"; do
    echo "  - $DOCKER_REGISTRY/$service:$TAG"
done

echo ""
echo "Next steps:"
echo "  1. Push images to registry (if needed): ./push-images.sh"
echo "  2. Deploy to Kubernetes: ./deploy-all.sh"
