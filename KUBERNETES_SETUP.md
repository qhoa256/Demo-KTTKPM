# 🚀 Hướng dẫn chạy Costume Rental System trên Kubernetes

## 📋 Tổng quan

Tôi đã tạo một bộ deployment Kubernetes hoàn chỉnh cho hệ thống Costume Rental System. Bộ deployment này bao gồm:

- ✅ **6 microservices** với Dockerfiles
- ✅ **MySQL database** với persistent storage
- ✅ **Kubernetes manifests** đầy đủ
- ✅ **Ingress configuration** cho external access
- ✅ **Scripts tự động** cho build và deploy
- ✅ **Health checks** và monitoring
- ✅ **ConfigMaps và Secrets** management

## 🎯 Cách chạy nhanh (Quick Start)

### 1. Chuẩn bị môi trường
```bash
cd Demo-KTTKPM/k8s/scripts
./setup.sh
```

### 2. Build và Deploy
```bash
./build-all.sh    # Build tất cả Docker images
./deploy-all.sh   # Deploy lên Kubernetes
```

### 3. Kiểm tra và truy cập
```bash
./check-status.sh # Kiểm tra trạng thái
```

### 4. Truy cập ứng dụng
- **Frontend**: http://costume-rental.local
- **API**: http://costume-rental.local/api

## 🔧 Yêu cầu hệ thống

### Phần mềm cần thiết:
- **Kubernetes cluster** (Minikube, Kind, hoặc production cluster)
- **kubectl** configured
- **Docker** (v20.10+)
- **Maven** (v3.6+)
- **Java** 17+

### Tài nguyên tối thiểu:
- **CPU**: 4 cores
- **Memory**: 8GB RAM
- **Storage**: 20GB

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
