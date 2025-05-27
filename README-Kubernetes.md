# Kubernetes Deployment Guide

## Prerequisites

1. **Docker Desktop** with Kubernetes enabled
2. **kubectl** command line tool
3. **Windows Command Prompt** (CMD)

## Quick Start

### 1. Deploy to Kubernetes
```cmd
deploy-k8s.cmd
```

This script will:
- Build all Docker images
- Create Kubernetes namespace
- Deploy MySQL database
- Deploy all microservices in correct order
- Set up ingress for external access

### 2. Check Deployment Status
```cmd
check-k8s-status.cmd
```

### 3. Access the Application

#### Option A: LoadBalancer (Recommended for Docker Desktop)
The application is automatically available at: **http://localhost:8080**

#### Option B: Port Forwarding
```cmd
port-forward-k8s.cmd
```
Then open: http://localhost:8080

### 4. Start/Stop Services

#### Start Services
```cmd
start-k8s.cmd
```

#### Stop Services
```cmd
stop-k8s.cmd
```

#### Restart Services
```cmd
restart-k8s.cmd
```

#### Management Menu (All-in-One)
```cmd
k8s-menu.cmd
```

### 5. Additional Management Scripts

#### View Logs
```cmd
view-logs-k8s.cmd
```

#### Scale Services
```cmd
scale-k8s.cmd
```

#### Cleanup
```cmd
cleanup-k8s.cmd
```

## ✅ Deployment Status

**DEPLOYMENT COMPLETED SUCCESSFULLY!**

All services are running and accessible:
- ✅ MySQL Database
- ✅ User Service (8081)
- ✅ Costume Service (8082)
- ✅ Bill Costume Service (8083)
- ✅ Supplier Service (8084)
- ✅ Import Bill Service (8085)
- ✅ Client Application (8080)

**Access URL: http://localhost:8080**

## 🎮 Quản lý Kubernetes Services

### Cách sử dụng đơn giản:

#### 1. **Menu tổng hợp (Khuyến nghị)**
```cmd
k8s-menu.cmd
```
Script này cung cấp menu tương tác với tất cả các tùy chọn quản lý.

#### 2. **Khởi động services**
```cmd
start-k8s.cmd
```
- Khởi động tất cả services đã deploy
- Tự động kiểm tra và deploy nếu chưa có
- Đợi cho đến khi tất cả services sẵn sàng

#### 3. **Dừng services**
```cmd
stop-k8s.cmd
```
- Dừng tất cả services (scale về 0 replicas)
- Giữ nguyên data trong database
- Không xóa cấu hình Kubernetes

#### 4. **Khởi động lại services**
```cmd
restart-k8s.cmd
```
- Dừng và khởi động lại tất cả services
- Hữu ích khi cần refresh services

### Các trường hợp sử dụng:

| Tình huống | Script sử dụng | Mô tả |
|------------|----------------|--------|
| Lần đầu deploy | `deploy-k8s.cmd` | Deploy toàn bộ từ đầu |
| Khởi động hàng ngày | `start-k8s.cmd` | Bật services đã có |
| Tạm dừng làm việc | `stop-k8s.cmd` | Tắt services, giữ data |
| Có vấn đề | `restart-k8s.cmd` | Khởi động lại |
| Quản lý tổng hợp | `k8s-menu.cmd` | Menu tương tác |
| Xóa hoàn toàn | `cleanup-k8s.cmd` | Xóa tất cả |

### Lưu ý quan trọng:

- **start-k8s.cmd**: Chỉ khởi động services, không build lại Docker images
- **stop-k8s.cmd**: Dừng services nhưng giữ nguyên data và cấu hình
- **deploy-k8s.cmd**: Build lại images và deploy từ đầu
- **cleanup-k8s.cmd**: Xóa hoàn toàn, mất hết data

## Architecture

The deployment includes:

### Services
- **client-costume-rental** (Frontend) - Port 8080
- **user-service** - Port 8081
- **costume-service** - Port 8082
- **bill-costume-service** - Port 8083
- **supplier-service** - Port 8084
- **import-bill-service** - Port 8085
- **mysql-service** - Port 3306

### Kubernetes Resources
- **Namespace**: costume-rental
- **ConfigMap**: Application configuration
- **Secret**: Database credentials
- **PersistentVolume**: MySQL data storage
- **Deployments**: All services
- **Services**: Internal communication
- **Ingress**: External access routing

## Manual Commands

### View Logs
```cmd
kubectl logs -f deployment/user-service -n costume-rental
kubectl logs -f deployment/costume-service -n costume-rental
kubectl logs -f deployment/mysql-deployment -n costume-rental
```

### Scale Services
```cmd
kubectl scale deployment user-service --replicas=2 -n costume-rental
```

### Update Image
```cmd
kubectl set image deployment/user-service user-service=user-service:v2 -n costume-rental
```

### Access Database
```cmd
kubectl exec -it deployment/mysql-deployment -n costume-rental -- mysql -u root -p1 costume_rental_db
```

## Troubleshooting

### Check Pod Status
```cmd
kubectl describe pod [pod-name] -n costume-rental
```

### Check Events
```cmd
kubectl get events -n costume-rental --sort-by='.lastTimestamp'
```

### Restart Service
```cmd
kubectl rollout restart deployment/[service-name] -n costume-rental
```

### Check Resource Usage
```cmd
kubectl top pods -n costume-rental
kubectl top nodes
```

## Configuration

### Environment Variables
All services use environment variables from:
- **ConfigMap**: `costume-rental-config`
- **Secret**: `costume-rental-secret`

### Database Configuration
- Host: mysql-service
- Port: 3306
- Database: costume_rental_db
- Username: root
- Password: 1 (from secret)

### Service Communication
Services communicate using internal DNS names:
- user-service:8081
- costume-service:8082
- bill-costume-service:8083
- supplier-service:8084
- import-bill-service:8085

## Monitoring

### Health Checks
All services have:
- **Liveness Probe**: Checks if service is running
- **Readiness Probe**: Checks if service is ready to receive traffic

### Resource Limits
Each service has:
- **Requests**: 512Mi memory, 250m CPU
- **Limits**: 1Gi memory, 500m CPU

## Security

- Services run as non-root users
- Database credentials stored in Kubernetes secrets
- Internal communication only (except frontend)
- Network policies can be added for additional security
