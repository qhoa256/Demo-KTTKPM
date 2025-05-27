# 🚀 Costume Rental System - Kubernetes Deployment Guide (Windows)

## 📋 Tổng quan hệ thống

Hệ thống **Costume Rental System** được triển khai trên Kubernetes với 6 microservices:

### 🏗️ Kiến trúc Microservices:
- **Frontend**: `client-costume-rental` (Spring Boot + Thymeleaf) - Port 8080
- **User Service**: `user-service` (API) - Port 8081
- **Costume Service**: `costume-service` (API) - Port 8082
- **Bill Service**: `bill-costume-service` (API) - Port 8083
- **Supplier Service**: `supplier-service` (API) - Port 8084
- **Import Bill Service**: `import-bill-service` (API) - Port 8085

### 🌐 Ingress Routing:
- **Frontend**: `http://costume-rental.local` → Spring Boot app
- **APIs**: `http://costume-rental.local/api/*` → Respective microservices

---

## 🛠️ Yêu cầu hệ thống (Windows)

### ✅ Phần mềm cần thiết:
1. **Docker Desktop for Windows** (latest version)
2. **Kubernetes** (enabled in Docker Desktop)
3. **kubectl** (command line tool)
4. **PowerShell** (Windows PowerShell hoặc PowerShell Core)
5. **Git** (để clone repository)

### 📦 Cài đặt Docker Desktop:
```powershell
# Download từ: https://www.docker.com/products/docker-desktop/
# Hoặc sử dụng Chocolatey:
choco install docker-desktop

# Sau khi cài đặt, enable Kubernetes trong Docker Desktop Settings
```

### 🔧 Cài đặt kubectl:
```powershell
# Sử dụng Chocolatey:
choco install kubernetes-cli

# Hoặc download trực tiếp:
# https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/
```

### 🌐 Cài đặt NGINX Ingress Controller:
```powershell
# Apply NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/cloud/deploy.yaml

# Đợi ingress controller ready
kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=120s
```

---

## 🚀 Hướng dẫn Deployment

### Bước 1: Clone Repository
```powershell
git clone <repository-url>
cd Demo-KTTKPM
```

### Bước 2: Cấu hình Hosts File
```powershell
# Mở PowerShell as Administrator
# Edit hosts file
notepad C:\Windows\System32\drivers\etc\hosts

# Thêm dòng này vào cuối file:
127.0.0.1 costume-rental.local
```

### Bước 3: Cấu hình PowerShell Execution Policy
```powershell
# Mở PowerShell as Administrator và chạy:
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Hoặc cho toàn hệ thống:
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope LocalMachine
```

### Bước 4: Build Docker Images
```powershell
cd k8s\scripts
.\build-all.ps1
```

### Bước 5: Deploy to Kubernetes
```powershell
.\deploy-simple.ps1
```

### Bước 6: Test Deployment
```powershell
# Test all endpoints
.\test-endpoints.ps1

# Manual verification
kubectl get pods -n costume-rental
kubectl get svc -n costume-rental
kubectl get ingress -n costume-rental
```

### Bước 7: Access Application
```powershell
# Mở browser
Start-Process "http://costume-rental.local"

# Hoặc test với PowerShell
Invoke-WebRequest -Uri "http://costume-rental.local" -UseBasicParsing
```

---

## 🔍 Testing & Verification

### 🌐 Frontend Access:
```powershell
# Mở browser hoặc test với PowerShell
Invoke-WebRequest -Uri "http://costume-rental.local" -UseBasicParsing
```

### 🔌 API Endpoints Testing:
```powershell
# Test User API
Invoke-RestMethod -Uri "http://costume-rental.local/api/users/" -Method GET

# Test Costume API
Invoke-RestMethod -Uri "http://costume-rental.local/api/costumes/" -Method GET

# Test Bill API
Invoke-RestMethod -Uri "http://costume-rental.local/api/bills/" -Method GET

# Test Supplier API
Invoke-RestMethod -Uri "http://costume-rental.local/api/suppliers/" -Method GET

# Test Import API
Invoke-RestMethod -Uri "http://costume-rental.local/api/import-bills/" -Method GET
```

---

## 🛠️ Troubleshooting (Windows)

### ❌ Vấn đề thường gặp:

#### 1. **Docker Desktop không start:**
```powershell
# Restart Docker Desktop
Stop-Process -Name "Docker Desktop" -Force
Start-Process "C:\Program Files\Docker\Docker\Docker Desktop.exe"
```

#### 2. **Kubernetes không available:**
```powershell
# Enable Kubernetes trong Docker Desktop Settings
# Settings → Kubernetes → Enable Kubernetes → Apply & Restart
```

#### 3. **Permission denied khi chạy scripts:**
```powershell
# Set execution policy
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

#### 4. **Hosts file không update được:**
```powershell
# Chạy PowerShell as Administrator
Start-Process powershell -Verb runAs
```

### 🔧 Debug Commands:
```powershell
# Check Docker
docker version
docker ps

# Check Kubernetes
kubectl cluster-info
kubectl get nodes

# Check pods logs
kubectl logs -f deployment/client-costume-rental -n costume-rental

# Port forward for direct access
kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
```

---

## 📊 Monitoring & Management

### 📈 Useful Commands:
```powershell
# Scale deployment
kubectl scale deployment client-costume-rental --replicas=2 -n costume-rental

# Update deployment
kubectl rollout restart deployment/client-costume-rental -n costume-rental

# Check deployment status
kubectl rollout status deployment/client-costume-rental -n costume-rental

# Delete deployment
kubectl delete namespace costume-rental
```

### 🔍 Logs Monitoring:
```powershell
# Real-time logs
kubectl logs -f deployment/client-costume-rental -n costume-rental

# Logs từ tất cả pods
kubectl logs -f -l app=client-costume-rental -n costume-rental
```

---

## 🎯 URLs và Endpoints

### 🌐 Frontend URLs:
- **Main**: http://costume-rental.local
- **Login**: http://costume-rental.local/login
- **Costumes**: http://costume-rental.local/costumes
- **Admin**: http://costume-rental.local/admin

### 🔌 API Endpoints:
- **User API**: http://costume-rental.local/api/users/
- **Costume API**: http://costume-rental.local/api/costumes/
- **Bill API**: http://costume-rental.local/api/bills/
- **Supplier API**: http://costume-rental.local/api/suppliers/
- **Import API**: http://costume-rental.local/api/import-bills/

---

## 🔄 Cleanup

### 🗑️ Xóa deployment:
```powershell
kubectl delete namespace costume-rental
```

### 🧹 Cleanup Docker:
```powershell
# Remove unused images
docker image prune -f

# Remove all costume-rental images
docker images | Select-String "costume-rental" | ForEach-Object { docker rmi ($_ -split '\s+')[2] }
```

---

## 📝 Notes cho Windows

### ⚠️ Khác biệt so với macOS:
1. **File paths**: Sử dụng `\` thay vì `/`
2. **Scripts**: PowerShell (`.ps1`) thay vì bash (`.sh`)
3. **Hosts file**: `C:\Windows\System32\drivers\etc\hosts`
4. **Line endings**: CRLF thay vì LF
5. **Permissions**: Cần "Run as Administrator" cho một số operations

### 💡 Tips:
- Sử dụng **Windows Terminal** để có trải nghiệm tốt hơn
- Enable **WSL2** trong Docker Desktop để performance tốt hơn
- Sử dụng **PowerShell Core** (7.x) thay vì Windows PowerShell (5.x) nếu có thể

---

## 🎉 Kết luận

Hệ thống Costume Rental đã được deploy thành công trên Windows với Kubernetes!

**Truy cập**: http://costume-rental.local để bắt đầu sử dụng hệ thống.
