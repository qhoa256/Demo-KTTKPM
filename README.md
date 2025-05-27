# 🎭 Online Costume Rental System

Hệ thống cho thuê trang phục trực tuyến được xây dựng theo kiến trúc microservices với Spring Boot và Kubernetes. Hệ thống cho phép khách hàng thuê trang phục online, quản lý việc nhập trang phục từ nhà cung cấp, và tạo báo cáo doanh thu theo danh mục trang phục.

## 🚀 Triển khai nhanh

### Kubernetes (Khuyến nghị)
```bash
cd Demo-KTTKPM/k8s/scripts
./setup.sh
./build-all.sh
./deploy-simple.sh
```

**Truy cập**: http://costume-rental.local

### Local Development
```bash
# Chạy từng service riêng (cần MySQL)
cd user-service && mvn spring-boot:run
cd costume-service && mvn spring-boot:run
cd bill-costume-service && mvn spring-boot:run
cd supplier-service && mvn spring-boot:run
cd import-bill-service && mvn spring-boot:run
cd client-costume-rental && mvn spring-boot:run
```

## Microservices Architecture

The system is divided into the following microservices:

### 1. User Service
Manages user-related information including customers and staff members.
- Database Tables: tblAddress, tblFullName, tblUser, tblCustomer, tblStaff

### 2. Costume Service
Manages costume information and records of costumes included in rental and importing bills.
- Database Tables: tblCostume, tblCostumeBill, tblCostumeImportingBill

### 3. Bill Costume Service
Manages rental bills and payment information.
- Database Tables: tblBill, tblPayment

### 4. Supplier Service
Manages information about costume suppliers.
- Database Tables: tblSupplier

### 5. Import Bill Service
Manages bills for importing costumes from suppliers.
- Database Tables: tblImportingBill

### Supporting Components
- API Gateway: Routes client requests to appropriate microservices
- Discovery Server: Service registry for service discovery
- Config Server: Centralized configuration management

## Technology Stack
- Backend: Spring Boot
- Frontend: HTML, CSS, JavaScript
- Database: MySQL (separate database for each microservice)

## Features
1. Customer costume rental
2. Costume import from suppliers
3. Revenue statistics by costume category