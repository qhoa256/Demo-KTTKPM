# 🚀 Costume Rental System - Windows PowerShell Scripts

## 📋 Tổng quan

Bộ scripts PowerShell để deploy và quản lý Costume Rental System trên Windows với Kubernetes.

## 📁 Cấu trúc Files

```
k8s/scripts/
├── build-all.ps1          # Build tất cả Docker images
├── deploy-simple.ps1      # Deploy lên Kubernetes
├── test-endpoints.ps1     # Test tất cả endpoints
├── cleanup.ps1            # Cleanup deployment và images
└── README_WINDOWS.md      # Hướng dẫn này
```

## 🛠️ Yêu cầu hệ thống

### ✅ Phần mềm cần thiết:
- **Windows 10/11** hoặc **Windows Server 2019+**
- **Docker Desktop for Windows** (latest version)
- **Kubernetes** (enabled trong Docker Desktop)
- **PowerShell 5.1+** hoặc **PowerShell Core 7.x**
- **kubectl** command line tool

### 🔧 Cài đặt nhanh:
```powershell
# Cài đặt Chocolatey (nếu chưa có)
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Cài đặt Docker Desktop và kubectl
choco install docker-desktop kubernetes-cli

# Enable Kubernetes trong Docker Desktop Settings
```

## 🚀 Quick Start

### 1️⃣ Chuẩn bị môi trường:
```powershell
# Set execution policy
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Thêm vào hosts file (Run as Administrator)
Add-Content -Path "C:\Windows\System32\drivers\etc\hosts" -Value "127.0.0.1 costume-rental.local"

# Verify Docker và Kubernetes
docker version
kubectl cluster-info
```

### 2️⃣ Build và Deploy:
```powershell
# Navigate to scripts directory
cd k8s\scripts

# Build all Docker images
.\build-all.ps1

# Deploy to Kubernetes
.\deploy-simple.ps1

# Test endpoints
.\test-endpoints.ps1
```

### 3️⃣ Access Application:
```powershell
# Open in browser
Start-Process "http://costume-rental.local"
```

## 📜 Chi tiết Scripts

### 🔨 build-all.ps1
**Mục đích**: Build tất cả Docker images cho microservices

**Cách sử dụng**:
```powershell
# Basic usage
.\build-all.ps1

# Custom registry và tag
.\build-all.ps1 -Registry "my-registry" -Tag "v1.0.0"
```

**Tính năng**:
- Build Spring Boot frontend từ source code thực sự
- Tạo API services với nginx và JSON responses
- Color output cho dễ theo dõi
- Error handling và validation

### 🚀 deploy-simple.ps1
**Mục đích**: Deploy tất cả services lên Kubernetes

**Cách sử dụng**:
```powershell
# Basic usage
.\deploy-simple.ps1

# Custom namespace và timeout
.\deploy-simple.ps1 -Namespace "my-namespace" -Timeout 180
```

**Tính năng**:
- Tạo namespace và ConfigMaps
- Deploy tất cả microservices
- Cấu hình Ingress với routing rules
- Wait for deployments ready
- Display deployment status

### 🧪 test-endpoints.ps1
**Mục đích**: Test tất cả frontend và API endpoints

**Cách sử dụng**:
```powershell
# Basic usage
.\test-endpoints.ps1

# Custom base URL
.\test-endpoints.ps1 -BaseUrl "http://my-domain.local"
```

**Tính năng**:
- Test frontend endpoints (HTML responses)
- Test API endpoints (JSON responses)
- Health check validation
- Detailed response preview
- Error reporting

### 🗑️ cleanup.ps1
**Mục đích**: Cleanup deployment và Docker images

**Cách sử dụng**:
```powershell
# Basic cleanup (chỉ Kubernetes resources)
.\cleanup.ps1

# Cleanup bao gồm Docker images
.\cleanup.ps1 -RemoveImages

# Force cleanup không confirmation
.\cleanup.ps1 -RemoveImages -Force

# Custom namespace
.\cleanup.ps1 -Namespace "my-namespace" -RemoveImages
```

**Tính năng**:
- Remove Kubernetes namespace và resources
- Optional Docker images cleanup
- Confirmation prompts (có thể skip với -Force)
- Verification steps
- Cleanup unused Docker resources

## 🔧 Troubleshooting

### ❌ Lỗi thường gặp:

#### 1. **Execution Policy Error**
```powershell
# Error: cannot be loaded because running scripts is disabled
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

#### 2. **Docker không chạy**
```powershell
# Start Docker Desktop
Start-Process "C:\Program Files\Docker\Docker\Docker Desktop.exe"

# Wait for Docker to start
do {
    Start-Sleep -Seconds 5
    $dockerRunning = docker version 2>$null
} while (-not $dockerRunning)
```

#### 3. **Kubernetes không available**
```powershell
# Enable trong Docker Desktop:
# Settings → Kubernetes → Enable Kubernetes → Apply & Restart
```

#### 4. **Permission denied cho hosts file**
```powershell
# Run PowerShell as Administrator
Start-Process powershell -Verb runAs
```

#### 5. **Port conflicts**
```powershell
# Check ports in use
netstat -an | Select-String ":80 "
netstat -an | Select-String ":8080 "

# Kill processes using ports
Get-Process -Id (Get-NetTCPConnection -LocalPort 80).OwningProcess | Stop-Process -Force
```

### 🔍 Debug Commands:
```powershell
# Check Docker
docker version
docker ps
docker images | Select-String "costume-rental"

# Check Kubernetes
kubectl cluster-info
kubectl get nodes
kubectl get all --all-namespaces

# Check specific deployment
kubectl get pods -n costume-rental
kubectl logs -f deployment/client-costume-rental -n costume-rental
kubectl describe pod <pod-name> -n costume-rental

# Port forward for direct access
kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
```

## 📊 Monitoring

### 📈 Useful Commands:
```powershell
# Real-time pod status
kubectl get pods -n costume-rental -w

# Logs from all pods
kubectl logs -f -l app=client-costume-rental -n costume-rental

# Resource usage
kubectl top pods -n costume-rental
kubectl top nodes

# Scale deployment
kubectl scale deployment client-costume-rental --replicas=3 -n costume-rental

# Rolling update
kubectl rollout restart deployment/client-costume-rental -n costume-rental
kubectl rollout status deployment/client-costume-rental -n costume-rental
```

## 🎯 Best Practices

### ✅ Recommendations:
1. **Sử dụng Windows Terminal** cho trải nghiệm tốt hơn
2. **Enable WSL2** trong Docker Desktop để performance tốt hơn
3. **Sử dụng PowerShell Core 7.x** thay vì Windows PowerShell 5.1
4. **Regular cleanup** Docker images để tiết kiệm disk space
5. **Monitor resource usage** để tránh system overload

### 🔒 Security:
```powershell
# Chỉ set execution policy cho current user
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Verify script signatures nếu cần
Get-AuthenticodeSignature .\build-all.ps1
```

## 🆘 Support

### 📞 Khi cần help:
1. Check logs: `kubectl logs -f deployment/<service-name> -n costume-rental`
2. Check events: `kubectl get events -n costume-rental --sort-by='.lastTimestamp'`
3. Check ingress: `kubectl describe ingress costume-rental-ingress -n costume-rental`
4. Port forward: `kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental`

### 🔗 Useful Links:
- [Docker Desktop for Windows](https://docs.docker.com/desktop/windows/)
- [Kubernetes on Windows](https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/)
- [PowerShell Documentation](https://docs.microsoft.com/en-us/powershell/)
- [NGINX Ingress Controller](https://kubernetes.github.io/ingress-nginx/)

---

## 🎉 Kết luận

Bộ scripts PowerShell này cung cấp một cách đơn giản và hiệu quả để deploy Costume Rental System trên Windows với Kubernetes. 

**Happy Deploying!** 🚀
