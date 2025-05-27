# Hướng dẫn triển khai Kubernetes cho Costume Rental System

## 🚀 Triển khai nhanh

### Bước 1: Chuẩn bị môi trường
```bash
cd Demo-KTTKPM/k8s/scripts
./setup.sh
```

### Bước 2: Build và Deploy
```bash
./build-all.sh
./deploy-all.sh
```

### Bước 3: Kiểm tra và truy cập
```bash
./check-status.sh
# Truy cập: http://costume-rental.local
```

## 📋 Yêu cầu hệ thống

### Phần mềm cần thiết
- **Kubernetes cluster** (v1.20+)
- **kubectl** configured
- **Docker** (v20.10+)
- **Maven** (v3.6+)
- **Java** 17+

### Tài nguyên cluster tối thiểu
- **CPU**: 4 cores
- **Memory**: 8GB RAM
- **Storage**: 20GB
- **Nodes**: 1+ (khuyến nghị 3+)

## 🔧 Triển khai từng bước

### 1. Chuẩn bị Kubernetes cluster

#### Minikube (Development)
```bash
minikube start --cpus=4 --memory=8192 --disk-size=20g
minikube addons enable ingress
```

#### Kind (Development)
```bash
kind create cluster --config=kind-config.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```

#### Production cluster
Đảm bảo cluster có:
- Ingress controller (NGINX khuyến nghị)
- Storage class cho PersistentVolumes
- Metrics server (cho HPA)

### 2. Cấu hình /etc/hosts
```bash
echo '127.0.0.1 costume-rental.local' | sudo tee -a /etc/hosts
```

### 3. Build Docker images
```bash
cd Demo-KTTKPM/k8s/scripts
./build-all.sh
```

Hoặc build từng service:
```bash
cd Demo-KTTKPM/user-service
docker build -t costume-rental/user-service:latest .
```

### 4. Deploy từng component

#### Database
```bash
kubectl apply -f k8s/namespace/
kubectl apply -f k8s/secrets/
kubectl apply -f k8s/configmaps/
kubectl apply -f k8s/database/
```

#### Services
```bash
kubectl apply -f k8s/services/user-service/
kubectl apply -f k8s/services/costume-service/
kubectl apply -f k8s/services/bill-costume-service/
kubectl apply -f k8s/services/supplier-service/
kubectl apply -f k8s/services/import-bill-service/
kubectl apply -f k8s/services/client-costume-rental/
```

#### Ingress
```bash
kubectl apply -f k8s/ingress/
```

## 🔍 Troubleshooting

### Vấn đề thường gặp

#### 1. Pods không start được
```bash
# Kiểm tra logs
kubectl logs -f deployment/user-service -n costume-rental

# Kiểm tra events
kubectl get events -n costume-rental --sort-by='.lastTimestamp'

# Kiểm tra pod description
kubectl describe pod <pod-name> -n costume-rental
```

#### 2. Database connection failed
```bash
# Kiểm tra MySQL pod
kubectl get pods -n costume-rental -l app=mysql

# Test connection
kubectl exec -it deployment/mysql -n costume-rental -- mysql -u root -p

# Kiểm tra service
kubectl get svc mysql -n costume-rental
```

#### 3. Service không accessible
```bash
# Kiểm tra service endpoints
kubectl get endpoints -n costume-rental

# Port forward để test
kubectl port-forward svc/user-service 8081:8081 -n costume-rental

# Test health endpoint
curl http://localhost:8081/actuator/health
```

#### 4. Ingress không hoạt động
```bash
# Kiểm tra ingress controller
kubectl get pods -n ingress-nginx

# Kiểm tra ingress
kubectl describe ingress costume-rental-ingress -n costume-rental

# Test với port-forward
kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
```

### Lỗi thường gặp và cách khắc phục

#### ImagePullBackOff
```bash
# Kiểm tra image có tồn tại
docker images | grep costume-rental

# Build lại image
./build-all.sh

# Hoặc pull từ registry
docker pull costume-rental/user-service:latest
```

#### CrashLoopBackOff
```bash
# Kiểm tra logs chi tiết
kubectl logs <pod-name> -n costume-rental --previous

# Thường do:
# - Database connection failed
# - Missing environment variables
# - Application startup errors
```

#### Pending Pods
```bash
# Kiểm tra resources
kubectl describe pod <pod-name> -n costume-rental

# Thường do:
# - Insufficient resources
# - PVC not bound
# - Node selector issues
```

## 📊 Monitoring và Logging

### Kiểm tra resource usage
```bash
kubectl top pods -n costume-rental
kubectl top nodes
```

### Xem logs realtime
```bash
# Tất cả pods
kubectl logs -f -l app=user-service -n costume-rental

# Specific pod
kubectl logs -f <pod-name> -n costume-rental
```

### Health checks
```bash
# Kiểm tra tất cả health endpoints
for service in user-service costume-service bill-costume-service supplier-service import-bill-service client-costume-rental; do
  echo "Checking $service..."
  kubectl exec -n costume-rental deployment/$service -- curl -s http://localhost:808*/actuator/health
done
```

## 🔄 Scaling và Updates

### Horizontal scaling
```bash
kubectl scale deployment user-service --replicas=3 -n costume-rental
```

### Rolling updates
```bash
kubectl set image deployment/user-service user-service=costume-rental/user-service:v2 -n costume-rental
kubectl rollout status deployment/user-service -n costume-rental
```

### Rollback
```bash
kubectl rollout undo deployment/user-service -n costume-rental
```

## 🧹 Cleanup

### Xóa toàn bộ deployment
```bash
./cleanup.sh
```

### Xóa từng phần
```bash
kubectl delete namespace costume-rental
kubectl delete pv mysql-pv
```

## 📈 Performance Tuning

### Database optimization
- Tăng connection pool size
- Optimize MySQL configuration
- Use read replicas

### Application optimization
- Increase JVM heap size
- Enable connection pooling
- Add caching layer

### Kubernetes optimization
- Set appropriate resource limits
- Use HPA for auto-scaling
- Implement pod disruption budgets
