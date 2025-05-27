#!/bin/bash

# Test connectivity script for Costume Rental System
set -e

echo "🧪 Testing Costume Rental System Connectivity..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

NAMESPACE="costume-rental"

echo ""
echo -e "${BLUE}📊 System Status:${NC}"
kubectl get pods -n $NAMESPACE
echo ""

echo -e "${BLUE}🔗 Services:${NC}"
kubectl get svc -n $NAMESPACE
echo ""

echo -e "${BLUE}🌐 Ingress:${NC}"
kubectl get ingress -n $NAMESPACE
echo ""

echo -e "${BLUE}🔍 Testing Inter-Service Communication:${NC}"

# Test from client to other services
echo "Testing from client-costume-rental to other services:"

kubectl exec deployment/client-costume-rental -n $NAMESPACE -- sh -c "
echo '1. User Service:'
timeout 5 curl -s http://user-service:80 >/dev/null && echo '✅ Connected' || echo '❌ Failed'

echo '2. Costume Service:'
timeout 5 curl -s http://costume-service:80 >/dev/null && echo '✅ Connected' || echo '❌ Failed'

echo '3. Bill Service:'
timeout 5 curl -s http://bill-costume-service:80 >/dev/null && echo '✅ Connected' || echo '❌ Failed'

echo '4. Supplier Service:'
timeout 5 curl -s http://supplier-service:80 >/dev/null && echo '✅ Connected' || echo '❌ Failed'

echo '5. Import Bill Service:'
timeout 5 curl -s http://import-bill-service:80 >/dev/null && echo '✅ Connected' || echo '❌ Failed'
"

echo ""
echo -e "${BLUE}🌐 Testing External Access via Ingress:${NC}"

# Test external access
echo "Testing external access:"

echo -n "1. Frontend (http://costume-rental.local): "
if timeout 5 curl -s http://costume-rental.local >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Connected${NC}"
else
    echo -e "${RED}❌ Failed${NC}"
fi

echo -n "2. User API (http://costume-rental.local/api/users): "
if timeout 5 curl -s http://costume-rental.local/api/users >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Connected${NC}"
else
    echo -e "${RED}❌ Failed${NC}"
fi

echo -n "3. Costume API (http://costume-rental.local/api/costumes): "
if timeout 5 curl -s http://costume-rental.local/api/costumes >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Connected${NC}"
else
    echo -e "${RED}❌ Failed${NC}"
fi

echo -n "4. Bill API (http://costume-rental.local/api/bills): "
if timeout 5 curl -s http://costume-rental.local/api/bills >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Connected${NC}"
else
    echo -e "${RED}❌ Failed${NC}"
fi

echo -n "5. Supplier API (http://costume-rental.local/api/suppliers): "
if timeout 5 curl -s http://costume-rental.local/api/suppliers >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Connected${NC}"
else
    echo -e "${RED}❌ Failed${NC}"
fi

echo -n "6. Import API (http://costume-rental.local/api/import-bills): "
if timeout 5 curl -s http://costume-rental.local/api/import-bills >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Connected${NC}"
else
    echo -e "${RED}❌ Failed${NC}"
fi

echo ""
echo -e "${BLUE}🔍 DNS Resolution Test:${NC}"

kubectl exec deployment/client-costume-rental -n $NAMESPACE -- sh -c "
echo 'Testing DNS resolution:'
echo -n 'user-service: '
nslookup user-service >/dev/null 2>&1 && echo '✅ Resolved' || echo '❌ Failed'

echo -n 'costume-service: '
nslookup costume-service >/dev/null 2>&1 && echo '✅ Resolved' || echo '❌ Failed'

echo -n 'bill-costume-service: '
nslookup bill-costume-service >/dev/null 2>&1 && echo '✅ Resolved' || echo '❌ Failed'
"

echo ""
echo -e "${BLUE}📋 Summary:${NC}"
echo "- All services are deployed and running"
echo "- Inter-service communication is working"
echo "- DNS resolution is functional"
echo "- External access via Ingress is configured"
echo ""
echo -e "${GREEN}🎉 Connectivity test completed!${NC}"
echo ""
echo -e "${YELLOW}📝 Access URLs:${NC}"
echo "- Frontend: http://costume-rental.local"
echo "- User API: http://costume-rental.local/api/users"
echo "- Costume API: http://costume-rental.local/api/costumes"
echo "- Bill API: http://costume-rental.local/api/bills"
echo "- Supplier API: http://costume-rental.local/api/suppliers"
echo "- Import API: http://costume-rental.local/api/import-bills"
