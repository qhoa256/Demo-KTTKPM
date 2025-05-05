# Costume Rental System - Project Summary

This project implements a microservices-based architecture for an Online Costume Rental System. The system allows customers to rent costumes online, manage costume imports from suppliers, and generate revenue reports by costume category.

## Architecture Overview

The project follows a microservice architecture pattern with the following components:

### 1. Discovery Server
- **Technology**: Spring Cloud Netflix Eureka
- **Purpose**: Service registry and discovery mechanism
- **Port**: 8761

### 2. Config Server
- **Technology**: Spring Cloud Config
- **Purpose**: Centralized configuration management
- **Port**: 8888
- **Configuration Source**: Local file system (could be replaced with Git repository)

### 3. API Gateway
- **Technology**: Spring Cloud Gateway
- **Purpose**: Single entry point for all client requests, request routing
- **Port**: 8080
- **Security**: OAuth2/JWT authentication

### 4. User Service
- **Port**: 8081
- **Purpose**: Manages user accounts including customers and staff
- **Database Tables**:
  - `tblAddress`: Stores address information
  - `tblFullName`: Stores user name information
  - `tblUser`: Stores user credentials and references
  - `tblCustomer`: Stores customer-specific information
  - `tblStaff`: Stores staff-specific information

### 5. Costume Service
- **Port**: 8082
- **Purpose**: Manages costume catalog and costume usage in bills
- **Database Tables**:
  - `tblCostume`: Stores costume details
  - `tblCostumeBill`: Stores costumes included in rental bills
  - `tblCostumeImportingBill`: Stores costumes included in import bills

### 6. Bill Costume Service
- **Purpose**: Manages rental bills and payment processing
- **Database Tables**:
  - `tblBill`: Stores bill information
  - `tblPayment`: Stores payment details

### 7. Supplier Service
- **Purpose**: Manages supplier information
- **Database Tables**:
  - `tblSupplier`: Stores supplier details

### 8. Import Bill Service
- **Purpose**: Manages bills for importing costumes
- **Database Tables**:
  - `tblImportingBill`: Stores import bill information

### 9. Frontend
- **Technology**: HTML, CSS, JavaScript, Bootstrap
- **Features**:
  - Costume browsing
  - Costume rental
  - Costume importing
  - Revenue statistics by category

## Database Design

Each microservice has its own dedicated database:

1. **User Service Database**:
   - One-to-one relationship between tblAddress and tblUser
   - One-to-one relationship between tblFullName and tblUser
   - One-to-one relationship between tblUser and tblCustomer
   - One-to-one relationship between tblUser and tblStaff

2. **Costume Service Database**:
   - One-to-many relationship between tblCostume and tblCostumeBill
   - One-to-many relationship between tblCostume and tblCostumeImportingBill

3. **Bill Costume Service Database**:
   - One-to-one relationship between tblBill and tblPayment

4. **Supplier Service Database**:
   - Single table for supplier information

5. **Import Bill Service Database**:
   - Single table for import bill information

## Service Communication

- Services communicate with each other through RESTful APIs
- Service discovery handled by Eureka
- Inter-service communication secured via OAuth2/JWT

## Deployment

Each service can be deployed independently as a JAR file:

```
java -jar <service-name>.jar
```

For full functionality, all services need to be started in the following order:
1. Discovery Server
2. Config Server
3. Other microservices
4. API Gateway

## Features Implemented

1. **Customer Costume Rental**:
   - Browse available costumes
   - Rent costumes with specific duration
   - Process payments

2. **Costume Import Management**:
   - Add new costumes from suppliers
   - Track costume import history

3. **Revenue Statistics**:
   - Generate reports by costume category

## Future Enhancements

1. Implement Docker containers for easier deployment
2. Add CI/CD pipeline
3. Enhance security with more granular permissions
4. Add inventory management features
5. Implement customer loyalty program 