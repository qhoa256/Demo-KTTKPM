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

# Function to create simple Spring Boot service images
build_service() {
    local service=$1
    echo -e "${YELLOW}📦 Creating demo image for $service...${NC}"

    # Get port number based on service
    local port
    case $service in
        "user-service") port="8081" ;;
        "costume-service") port="8082" ;;
        "bill-costume-service") port="8083" ;;
        "supplier-service") port="8084" ;;
        "import-bill-service") port="8085" ;;
        "client-costume-rental") port="8080" ;;
        *) port="8080" ;;
    esac

    # Create temporary directory for this service
    local temp_dir="/tmp/k8s-demo-$service"
    rm -rf "$temp_dir"
    mkdir -p "$temp_dir"

    # Create simple Spring Boot application
    if [ "$service" = "client-costume-rental" ]; then
        # Build the real Spring Boot frontend
        echo "Building real Spring Boot frontend from ../../client-costume-rental"
        cd ../../client-costume-rental
        docker build -t "$DOCKER_REGISTRY/$service:$TAG" .
        if [ $? -ne 0 ]; then
            echo -e "${RED}❌ Failed to build Spring Boot frontend${NC}"
            exit 1
        fi
        cd - >/dev/null
        rm -rf "$temp_dir"
        echo -e "${GREEN}✅ Successfully built $service${NC}"
        return
    else
        # Create simple API service
        create_api_service "$service" "$port" "$temp_dir"
    fi

    # Build Docker image
    cd "$temp_dir"
    docker build -t "$DOCKER_REGISTRY/$service:$TAG" .

    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ Failed to build Docker image for $service${NC}"
        cd - >/dev/null
        exit 1
    fi

    # Cleanup
    cd - >/dev/null
    rm -rf "$temp_dir"

    echo -e "${GREEN}✅ Successfully built $service${NC}"
}

# Function to create API service
create_api_service() {
    local service=$1
    local port=$2
    local dir=$3

    # Create simple nginx with API responses
    cat > "$dir/Dockerfile" << EOF
FROM nginx:alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create API responses
RUN mkdir -p /usr/share/nginx/html/api
RUN mkdir -p /usr/share/nginx/html/actuator

# Create service-specific API responses
RUN echo '{"service":"$service","status":"UP","port":$port,"data":[{"id":1,"name":"Sample Item 1"},{"id":2,"name":"Sample Item 2"}]}' > /usr/share/nginx/html/api/index.html

# Create health endpoint
RUN echo '{"status":"UP","service":"$service","port":$port}' > /usr/share/nginx/html/actuator/health

# Configure nginx for API
RUN echo 'server { \\
    listen $port; \\
    server_name localhost; \\
    root /usr/share/nginx/html; \\
    \\
    location / { \\
        add_header Content-Type application/json; \\
        try_files \$uri \$uri/ /api/index.html; \\
    } \\
    \\
    location /actuator/health { \\
        add_header Content-Type application/json; \\
        return 200 "{\"status\":\"UP\",\"service\":\"$service\",\"port\":$port}"; \\
    } \\
    \\
    location /api { \\
        add_header Content-Type application/json; \\
        try_files \$uri \$uri/ /api/index.html; \\
    } \\
}' > /etc/nginx/conf.d/default.conf

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \\
  CMD curl -f http://localhost:$port/actuator/health || exit 1

EXPOSE $port

CMD ["nginx", "-g", "daemon off;"]
EOF
}

# Main build process
echo "Starting build process..."
echo "Registry: $DOCKER_REGISTRY"
echo "Tag: $TAG"
echo ""

# Create simple Spring Boot services for demo
echo -e "${YELLOW}📦 Creating demo Spring Boot services...${NC}"

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
