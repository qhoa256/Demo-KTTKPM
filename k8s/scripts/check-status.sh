#!/bin/bash

# Status check script for Costume Rental System
set -e

echo "📊 Checking Costume Rental System status..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
NAMESPACE="costume-rental"

# Function to check service health
check_service_health() {
    local service=$1
    local port=$2
    
    echo -e "${BLUE}🔍 Checking $service health...${NC}"
    
    # Get pod status
    local pod_status=$(kubectl get pods -n $NAMESPACE -l app=$service -o jsonpath='{.items[0].status.phase}' 2>/dev/null)
    
    if [ "$pod_status" = "Running" ]; then
        echo -e "${GREEN}✅ $service pod is running${NC}"
        
        # Check readiness
        local ready=$(kubectl get pods -n $NAMESPACE -l app=$service -o jsonpath='{.items[0].status.conditions[?(@.type=="Ready")].status}' 2>/dev/null)
        if [ "$ready" = "True" ]; then
            echo -e "${GREEN}✅ $service is ready${NC}"
        else
            echo -e "${YELLOW}⚠️ $service is not ready${NC}"
        fi
        
        # Try health check endpoint
        if command -v curl > /dev/null; then
            local pod_name=$(kubectl get pods -n $NAMESPACE -l app=$service -o jsonpath='{.items[0].metadata.name}' 2>/dev/null)
            if [ ! -z "$pod_name" ]; then
                kubectl exec -n $NAMESPACE $pod_name -- curl -s http://localhost:$port/actuator/health > /dev/null 2>&1
                if [ $? -eq 0 ]; then
                    echo -e "${GREEN}✅ $service health endpoint responding${NC}"
                else
                    echo -e "${YELLOW}⚠️ $service health endpoint not responding${NC}"
                fi
            fi
        fi
    else
        echo -e "${RED}❌ $service pod status: $pod_status${NC}"
    fi
    echo ""
}

# Check if namespace exists
if ! kubectl get namespace $NAMESPACE > /dev/null 2>&1; then
    echo -e "${RED}❌ Namespace $NAMESPACE does not exist${NC}"
    exit 1
fi

echo "Namespace: $NAMESPACE"
echo ""

# Overall cluster status
echo -e "${BLUE}🏗️ Overall Status:${NC}"
kubectl get all -n $NAMESPACE
echo ""

# Check each service
echo -e "${BLUE}🔍 Service Health Checks:${NC}"
check_service_health "mysql" "3306"
check_service_health "user-service" "8081"
check_service_health "costume-service" "8082"
check_service_health "bill-costume-service" "8083"
check_service_health "supplier-service" "8084"
check_service_health "import-bill-service" "8085"
check_service_health "client-costume-rental" "8080"

# Check ingress
echo -e "${BLUE}🌐 Ingress Status:${NC}"
kubectl get ingress -n $NAMESPACE
echo ""

# Check persistent volumes
echo -e "${BLUE}💾 Storage Status:${NC}"
kubectl get pv,pvc -n $NAMESPACE
echo ""

# Resource usage
echo -e "${BLUE}📈 Resource Usage:${NC}"
kubectl top pods -n $NAMESPACE 2>/dev/null || echo "Metrics server not available"
echo ""

# Recent events
echo -e "${BLUE}📋 Recent Events:${NC}"
kubectl get events -n $NAMESPACE --sort-by='.lastTimestamp' | tail -10
echo ""

# Connection test
echo -e "${BLUE}🔗 Connection Test:${NC}"
if command -v curl > /dev/null; then
    # Test if ingress is accessible
    if grep -q "costume-rental.local" /etc/hosts 2>/dev/null; then
        echo "Testing http://costume-rental.local..."
        curl -s -o /dev/null -w "HTTP Status: %{http_code}\n" http://costume-rental.local || echo "Connection failed"
    else
        echo -e "${YELLOW}⚠️ Add '127.0.0.1 costume-rental.local' to /etc/hosts to test ingress${NC}"
    fi
else
    echo "curl not available for connection testing"
fi

echo ""
echo -e "${BLUE}🔧 Useful Commands:${NC}"
echo "- View logs: kubectl logs -f deployment/<service-name> -n $NAMESPACE"
echo "- Port forward: kubectl port-forward svc/client-costume-rental 8080:8080 -n $NAMESPACE"
echo "- Scale service: kubectl scale deployment <service-name> --replicas=3 -n $NAMESPACE"
echo "- Restart deployment: kubectl rollout restart deployment/<service-name> -n $NAMESPACE"
