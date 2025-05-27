# ⚡ Quick Start - Costume Rental System

## 🚀 Triển khai trong 5 phút

### 1. Chuẩn bị
```bash
cd Demo-KTTKPM/k8s/scripts
./setup.sh
```

### 2. Build & Deploy
```bash
./build-all.sh
./deploy-simple.sh
```

### 3. Truy cập
- **Frontend**: http://costume-rental.local
- **APIs**: http://costume-rental.local/api/*

## 📊 Services & Ports

| Service | Port | URL |
|---------|------|-----|
| Frontend | 8080 | http://costume-rental.local |
| User API | 8081 | http://costume-rental.local/api/users |
| Costume API | 8082 | http://costume-rental.local/api/costumes |
| Bill API | 8083 | http://costume-rental.local/api/bills |
| Supplier API | 8084 | http://costume-rental.local/api/suppliers |
| Import API | 8085 | http://costume-rental.local/api/import-bills |

## 🔧 Commands thường dùng

### Kiểm tra status
```bash
kubectl get pods -n costume-rental
kubectl get svc -n costume-rental
kubectl get ingress -n costume-rental
```

### Xem logs
```bash
kubectl logs -f deployment/client-costume-rental -n costume-rental
kubectl logs -f deployment/user-service -n costume-rental
```

### Port forwarding
```bash
# Frontend
kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental

# User Service
kubectl port-forward svc/user-service 8081:8081 -n costume-rental
```

### Scaling
```bash
kubectl scale deployment user-service --replicas=3 -n costume-rental
```

### Cleanup
```bash
kubectl delete namespace costume-rental
```

## 🚨 Troubleshooting

### Pods không chạy
```bash
kubectl describe pod <pod-name> -n costume-rental
kubectl logs <pod-name> -n costume-rental
```

### Ingress không hoạt động
```bash
kubectl get pods -n ingress-nginx
kubectl describe ingress costume-rental-ingress -n costume-rental
```

### Test connectivity
```bash
curl http://costume-rental.local
curl http://costume-rental.local/api/users
```

## 📖 Chi tiết

Xem file `KUBERNETES_SETUP.md` để có hướng dẫn chi tiết đầy đủ.
