# Online Costume Rental System

This project implements a microservices-based architecture for an Online Costume Rental System. The system allows customers to rent costumes online, manage costume imports from suppliers, and generate revenue reports by costume category.

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