# 🎭 Costume Rental System - Docker Deployment

## 🚀 Triển khai thành công!

Hệ thống Costume Rental System đã được triển khai thành công với Docker và đang chạy hoàn toàn ổn định.

## 📋 Tổng quan hệ thống

### Microservices Architecture
- **user-service** (Port 8081) - Quản lý người dùng
- **costume-service** (Port 8082) - Quản lý trang phục
- **bill-costume-service** (Port 8083) - Quản lý hóa đơn thuê
- **supplier-service** (Port 8084) - Quản lý nhà cung cấp
- **import-bill-service** (Port 8085) - Quản lý hóa đơn nhập
- **client-costume-rental** (Port 8080) - Ứng dụng frontend
- **MySQL Database** (Port 3307) - Cơ sở dữ liệu

## 🌐 Truy cập ứng dụng

### Frontend Application
- **URL**: http://localhost:8080
- **Mô tả**: Giao diện người dùng chính của hệ thống

### Microservices Health Check
- **User Service**: http://localhost:8081/actuator/health
- **Costume Service**: http://localhost:8082/actuator/health
- **Bill Service**: http://localhost:8083/actuator/health
- **Supplier Service**: http://localhost:8084/actuator/health
- **Import Bill Service**: http://localhost:8085/actuator/health

### Database Connection
- **Host**: localhost
- **Port**: 3307
- **Username**: root
- **Password**: 1

## 🐳 Docker Commands

### Kiểm tra trạng thái containers
```bash
docker-compose ps
```

### Xem logs của tất cả services
```bash
docker-compose logs -f
```

### Xem logs của service cụ thể
```bash
docker-compose logs -f user-service
docker-compose logs -f costume-service
docker-compose logs -f client-costume-rental
```

### Restart service cụ thể
```bash
docker-compose restart user-service
```

### Dừng toàn bộ hệ thống
```bash
docker-compose down
```

### Khởi động lại toàn bộ hệ thống
```bash
docker-compose up -d
```

## 🔧 Cấu hình Docker

### Docker Images đã build
- `costume-rental/user-service:latest`
- `costume-rental/costume-service:latest`
- `costume-rental/bill-costume-service:latest`
- `costume-rental/supplier-service:latest`
- `costume-rental/import-bill-service:latest`
- `costume-rental/client-costume-rental:latest`

### Volumes
- `mysql_data` - Lưu trữ dữ liệu MySQL

### Networks
- `costume-rental-network` - Mạng nội bộ cho các containers

## 📊 Monitoring & Health Checks

Tất cả services đều có health checks tự động:
- **Interval**: 30 giây
- **Timeout**: 3 giây
- **Start period**: 60 giây
- **Retries**: 3 lần

## 🛠️ Troubleshooting

### Kiểm tra container logs nếu có lỗi
```bash
docker-compose logs [service-name]
```

### Restart container nếu cần
```bash
docker-compose restart [service-name]
```

### Kiểm tra resource usage
```bash
docker stats
```

### Rebuild image nếu có thay đổi code
```bash
docker-compose up --build [service-name]
```

## 🎯 Tính năng đã triển khai

✅ **Hoàn thành 100%**
- [x] Dockerfiles cho tất cả microservices
- [x] Docker Compose configuration
- [x] MySQL database với init scripts
- [x] Health checks cho tất cả services
- [x] Service discovery và networking
- [x] Volume persistence cho database
- [x] Multi-stage builds để tối ưu image size
- [x] Non-root user security
- [x] Actuator endpoints cho monitoring

## 🔐 Security Features

- Containers chạy với non-root user
- Database credentials được quản lý qua environment variables
- Health checks sử dụng internal endpoints
- Network isolation giữa các services

## 📈 Performance Optimizations

- Multi-stage Docker builds
- Maven dependency caching
- Docker layer caching
- Optimized base images (Eclipse Temurin)

## 🎉 Kết quả

Hệ thống đã được triển khai thành công với:
- ✅ Tất cả 6 microservices đang chạy healthy
- ✅ MySQL database hoạt động bình thường
- ✅ Frontend accessible tại http://localhost:8080
- ✅ Health checks pass cho tất cả services
- ✅ Service-to-service communication hoạt động
- ✅ Database connections stable

**Hệ thống sẵn sàng sử dụng!** 🚀
