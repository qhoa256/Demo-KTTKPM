# 🚀 Hướng dẫn triển khai Costume Rental System trên Kubernetes

## 📋 Tổng quan hệ thống

Costume Rental System là một hệ thống cho thuê trang phục trực tuyến được xây dựng theo kiến trúc microservices, bao gồm:

### 🏗️ Kiến trúc microservices:
- ✅ **6 microservices** độc lập
- ✅ **Kubernetes deployment** hoàn chỉnh
- ✅ **Ingress configuration** cho external access
- ✅ **Scripts tự động** cho build và deploy
- ✅ **Health checks** và monitoring
- ✅ **ConfigMaps** cho configuration management

### 🔧 Các services và ports:

| Service | Port | Mô tả | URL |
|---------|------|-------|-----|
| **Frontend (Client)** | 8080 | Giao diện người dùng | http://costume-rental.local |
| **User Service** | 8081 | Quản lý người dùng | http://costume-rental.local/api/users |
| **Costume Service** | 8082 | Quản lý trang phục | http://costume-rental.local/api/costumes |
| **Bill Service** | 8083 | Quản lý hóa đơn | http://costume-rental.local/api/bills |
| **Supplier Service** | 8084 | Quản lý nhà cung cấp | http://costume-rental.local/api/suppliers |
| **Import Bill Service** | 8085 | Quản lý nhập hàng | http://costume-rental.local/api/import-bills |

## 🎯 Triển khai nhanh (Quick Start)

### Bước 1: Chuẩn bị môi trường
```bash
cd Demo-KTTKPM/k8s/scripts
./setup.sh
```

### Bước 2: Build Docker images
```bash
./build-all.sh
```

### Bước 3: Deploy lên Kubernetes
```bash
./deploy-simple.sh
```

### Bước 4: Kiểm tra trạng thái
```bash
./check-status.sh
```

### Bước 5: Truy cập ứng dụng
- **Frontend**: http://costume-rental.local
- **API Endpoints**: http://costume-rental.local/api/*

## 🔧 Yêu cầu hệ thống

### Phần mềm cần thiết:
- **Kubernetes cluster** (Minikube, Kind, hoặc production cluster)
- **kubectl** configured và connected
- **Docker** (v20.10+)
- **NGINX Ingress Controller** (sẽ được cài tự động)

### Tài nguyên tối thiểu:
- **CPU**: 2 cores
- **Memory**: 4GB RAM
- **Storage**: 10GB

## 🧪 Hướng dẫn test và truy cập

### 1. Kiểm tra deployment status
```bash
# Kiểm tra tất cả pods
kubectl get pods -n costume-rental

# Kiểm tra services
kubectl get svc -n costume-rental

# Kiểm tra ingress
kubectl get ingress -n costume-rental
```

### 2. Test từng service riêng lẻ

#### Frontend (Port 8080)
```bash
# Truy cập qua ingress
curl http://costume-rental.local

# Hoặc port-forward
kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
# Sau đó truy cập: http://localhost:8080
```

#### Backend APIs

**User Service (Port 8081)**
```bash
# Qua ingress
curl http://costume-rental.local/api/users

# Port-forward
kubectl port-forward svc/user-service 8081:8081 -n costume-rental
curl http://localhost:8081
```

**Costume Service (Port 8082)**
```bash
# Qua ingress
curl http://costume-rental.local/api/costumes

# Port-forward
kubectl port-forward svc/costume-service 8082:8082 -n costume-rental
curl http://localhost:8082
```

**Bill Service (Port 8083)**
```bash
# Qua ingress
curl http://costume-rental.local/api/bills

# Port-forward
kubectl port-forward svc/bill-costume-service 8083:8083 -n costume-rental
curl http://localhost:8083
```

**Supplier Service (Port 8084)**
```bash
# Qua ingress
curl http://costume-rental.local/api/suppliers

# Port-forward
kubectl port-forward svc/supplier-service 8084:8084 -n costume-rental
curl http://localhost:8084
```

**Import Bill Service (Port 8085)**
```bash
# Qua ingress
curl http://costume-rental.local/api/import-bills

# Port-forward
kubectl port-forward svc/import-bill-service 8085:8085 -n costume-rental
curl http://localhost:8085
```

### 3. Monitoring và logs

#### Xem logs realtime
```bash
# Logs của frontend
kubectl logs -f deployment/client-costume-rental -n costume-rental

# Logs của user service
kubectl logs -f deployment/user-service -n costume-rental

# Logs của tất cả pods
kubectl logs -f -l app=user-service -n costume-rental
```

#### Kiểm tra resource usage
```bash
# Resource usage của pods
kubectl top pods -n costume-rental

# Resource usage của nodes
kubectl top nodes
```

#### Health checks
```bash
# Kiểm tra pod status chi tiết
kubectl describe pod <pod-name> -n costume-rental

# Kiểm tra events
kubectl get events -n costume-rental --sort-by='.lastTimestamp'
```

## 🚨 Troubleshooting

### Vấn đề thường gặp

#### 1. Pods không start được
```bash
# Xem logs chi tiết
kubectl logs <pod-name> -n costume-rental

# Xem mô tả pod
kubectl describe pod <pod-name> -n costume-rental

# Xem events
kubectl get events -n costume-rental
```

#### 2. Ingress không hoạt động
```bash
# Kiểm tra ingress controller
kubectl get pods -n ingress-nginx

# Kiểm tra ingress configuration
kubectl describe ingress costume-rental-ingress -n costume-rental

# Test bằng port-forward
kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
```

#### 3. Service không accessible
```bash
# Kiểm tra endpoints
kubectl get endpoints -n costume-rental

# Test service connectivity
kubectl exec -it <pod-name> -n costume-rental -- curl http://user-service:8081
```

## 🔄 Scaling và Management

### Horizontal scaling
```bash
# Scale user service lên 3 replicas
kubectl scale deployment user-service --replicas=3 -n costume-rental

# Scale tất cả services
kubectl scale deployment --all --replicas=2 -n costume-rental
```

### Rolling updates
```bash
# Update image
kubectl set image deployment/user-service user-service=costume-rental/user-service:v2 -n costume-rental

# Kiểm tra rollout status
kubectl rollout status deployment/user-service -n costume-rental

# Rollback nếu cần
kubectl rollout undo deployment/user-service -n costume-rental
```

### Cleanup
```bash
# Xóa toàn bộ deployment
kubectl delete namespace costume-rental

# Hoặc dùng script
./cleanup.sh
```

## 🏗️ Kiến trúc Kubernetes

```
┌─────────────────────────────────────────────────────────────┐
│                    Ingress Controller                       │
│                costume-rental.local                        │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────┴───────────────────────────────────────┐
│                 Kubernetes Cluster                         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  Namespace: costume-rental          │   │
│  │                                                     │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │   │
│  │  │ User Service│  │Costume Svc  │  │ Bill Service│ │   │
│  │  │   :8081     │  │   :8082     │  │   :8083     │ │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘ │   │
│  │                                                     │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │   │
│  │  │Supplier Svc │  │Import Bill  │  │   Client    │ │   │
│  │  │   :8084     │  │   :8085     │  │   :8080     │ │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘ │   │
│  │                                                     │   │
│  │  ┌─────────────────────────────────────────────────┐ │   │
│  │  │              MySQL Database                     │ │   │
│  │  │                 :3306                           │ │   │
│  │  │        (5 separate databases)                   │ │   │
│  │  └─────────────────────────────────────────────────┘ │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## 📁 Cấu trúc files đã tạo

```
Demo-KTTKPM/
├── k8s/                              # Kubernetes configurations
│   ├── README.md                     # Hướng dẫn chi tiết
│   ├── DEPLOYMENT_GUIDE.md           # Troubleshooting guide
│   ├── namespace/                    # Namespace và resource quotas
│   ├── secrets/                      # Database credentials
│   ├── configmaps/                   # Application configurations
│   ├── database/                     # MySQL setup
│   ├── services/                     # Service deployments
│   │   ├── user-service/
│   │   ├── costume-service/
│   │   ├── bill-costume-service/
│   │   ├── supplier-service/
│   │   ├── import-bill-service/
│   │   └── client-costume-rental/
│   ├── ingress/                      # External access
│   └── scripts/                      # Automation scripts
│       ├── setup.sh                  # Environment setup
│       ├── build-all.sh              # Build all images
│       ├── deploy-all.sh             # Deploy to K8s
│       ├── check-status.sh           # Status monitoring
│       └── cleanup.sh                # Remove all resources
├── user-service/
│   ├── Dockerfile                    # Docker image
│   └── src/main/resources/
│       └── application-kubernetes.properties
├── costume-service/
│   ├── Dockerfile
│   └── src/main/resources/
│       └── application-kubernetes.properties
├── bill-costume-service/
│   ├── Dockerfile
│   └── src/main/resources/
│       └── application-kubernetes.properties
├── supplier-service/
│   ├── Dockerfile
│   └── src/main/resources/
│       └── application-kubernetes.properties
├── import-bill-service/
│   ├── Dockerfile
│   └── src/main/resources/
│       └── application-kubernetes.properties
└── client-costume-rental/
    ├── Dockerfile
    └── src/main/resources/
        └── application-kubernetes.properties
```

## 🔍 Các vấn đề đã khắc phục

### 1. **Service Discovery**
- Sử dụng Kubernetes DNS cho inter-service communication
- ConfigMaps để manage service URLs

### 2. **Database Management**
- MySQL với persistent volumes
- Separate databases cho từng service
- Auto-initialization scripts

### 3. **Configuration Management**
- Environment-specific properties files
- Secrets cho sensitive data
- ConfigMaps cho application settings

### 4. **Health Checks**
- Liveness và readiness probes
- Actuator endpoints
- Graceful shutdown

### 5. **Networking**
- Ingress cho external access
- Service mesh ready
- Load balancing

## 🚨 Troubleshooting thường gặp

### Pods không start
```bash
kubectl logs -f deployment/user-service -n costume-rental
kubectl describe pod <pod-name> -n costume-rental
```

### Database connection issues
```bash
kubectl exec -it deployment/mysql -n costume-rental -- mysql -u root -p
```

### Ingress không hoạt động
```bash
# Kiểm tra ingress controller
kubectl get pods -n ingress-nginx

# Port forward để test
kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
```

## 📊 Monitoring

### Kiểm tra status
```bash
./check-status.sh
kubectl get all -n costume-rental
kubectl top pods -n costume-rental
```

### Xem logs
```bash
kubectl logs -f deployment/client-costume-rental -n costume-rental
```

## 🔄 Scaling

### Manual scaling
```bash
kubectl scale deployment user-service --replicas=3 -n costume-rental
```

### Auto-scaling (HPA)
```bash
kubectl autoscale deployment user-service --cpu-percent=70 --min=2 --max=10 -n costume-rental
```

## 🧹 Cleanup

### Xóa toàn bộ
```bash
./cleanup.sh
```

### Xóa từng phần
```bash
kubectl delete namespace costume-rental
```

## 📞 Hỗ trợ

Nếu gặp vấn đề:
1. Kiểm tra `k8s/DEPLOYMENT_GUIDE.md` cho troubleshooting chi tiết
2. Chạy `./check-status.sh` để kiểm tra trạng thái
3. Xem logs của từng service
4. Kiểm tra Kubernetes events

## 🎉 Kết luận

Bộ deployment này đã sẵn sàng cho:
- ✅ **Development environment**
- ✅ **Testing environment**
- ✅ **Production environment** (với một số điều chỉnh)

Hệ thống sẽ tự động:
- Build Docker images
- Deploy tất cả services
- Setup database
- Configure networking
- Enable health monitoring
