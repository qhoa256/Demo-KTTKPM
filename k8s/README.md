# Kubernetes Deployment cho Costume Rental System

## Tổng quan

Thư mục này chứa tất cả các file cấu hình Kubernetes để triển khai hệ thống Costume Rental System lên Kubernetes cluster.

## Cấu trúc thư mục

```
k8s/
├── README.md                    # File này
├── namespace/                   # Namespace configuration
├── database/                    # MySQL database setup
├── configmaps/                  # ConfigMaps cho các services
├── secrets/                     # Secrets cho database credentials
├── services/                    # Service definitions
│   ├── user-service/
│   ├── costume-service/
│   ├── bill-costume-service/
│   ├── supplier-service/
│   ├── import-bill-service/
│   └── client-costume-rental/
├── ingress/                     # Ingress configuration
├── scripts/                     # Build và deployment scripts
└── docker/                      # Dockerfiles cho từng service
```

## Yêu cầu hệ thống

- Kubernetes cluster (v1.20+)
- kubectl configured
- Docker registry access
- Ingress controller (nginx recommended)
- Persistent Volume support

## Triển khai nhanh

1. **Build Docker images:**
   ```bash
   ./scripts/build-all.sh
   ```

2. **Deploy to Kubernetes:**
   ```bash
   ./scripts/deploy-all.sh
   ```

3. **Kiểm tra deployment:**
   ```bash
   kubectl get pods -n costume-rental
   ```

## Triển khai từng bước

### 1. Tạo namespace
```bash
kubectl apply -f namespace/
```

### 2. Deploy database
```bash
kubectl apply -f database/
```

### 3. Deploy ConfigMaps và Secrets
```bash
kubectl apply -f configmaps/
kubectl apply -f secrets/
```

### 4. Deploy services
```bash
kubectl apply -f services/
```

### 5. Deploy Ingress
```bash
kubectl apply -f ingress/
```

## Truy cập ứng dụng

Sau khi deploy thành công:
- Frontend: http://costume-rental.local
- API Gateway: http://costume-rental.local/api

## Troubleshooting

### Kiểm tra logs
```bash
kubectl logs -f deployment/user-service -n costume-rental
```

### Kiểm tra database connection
```bash
kubectl exec -it deployment/mysql -n costume-rental -- mysql -u root -p
```

### Port forwarding để test local
```bash
kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
```

## Scaling

### Scale services
```bash
kubectl scale deployment user-service --replicas=3 -n costume-rental
```

### Auto-scaling (HPA)
```bash
kubectl apply -f hpa/
```

## Monitoring

### Kiểm tra resource usage
```bash
kubectl top pods -n costume-rental
```

### Kiểm tra events
```bash
kubectl get events -n costume-rental --sort-by='.lastTimestamp'
```
