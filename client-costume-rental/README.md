# Costume Rental Client Application

A Spring Boot web application for the Quang Hoa Costume Rental Shop.

## Project Structure

```
client-costume-rental/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── quanghoa/
│   │   │           └── costumeservice/
│   │   │               ├── controller/
│   │   │               │   └── ClientController.java
│   │   │               ├── model/
│   │   │               │   ├── LoginRequest.java
│   │   │               │   └── LoginResponse.java
│   │   │               ├── service/
│   │   │               │   ├── UserService.java
│   │   │               │   └── UserServiceImpl.java
│   │   │               └── CostumeClientApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── login.html
│   │       │   ├── customer_home.html
│   │       │   └── admin.html
│   │       ├── static/
│   │       └── application.properties
└── pom.xml
```

## Features

- User authentication via the user-service
- Role-based redirection to appropriate pages (customer or admin)
- Modern responsive UI using Tailwind CSS

## Dependencies

- Spring Boot 2.7.x
- Spring Web
- Thymeleaf
- Lombok

## Configuration

The application connects to the user-service API for authentication. You can configure the service URL in `application.properties`:

```properties
user.service.url=http://localhost:8081
```

## Running the application

1. Make sure the user-service is running at the configured URL
2. Build the project:
   ```
   mvn clean package
   ```
3. Run the application:
   ```
   java -jar target/client-costume-rental-0.0.1-SNAPSHOT.jar
   ```
4. Access the application at http://localhost:8080

## API Integration

The client connects to the following endpoints:
- `POST /api/users/login` - Authenticates users and returns their role

## User Flow

1. User accesses the application and is redirected to the login page
2. After successful authentication, user is redirected based on their role:
   - Customers are redirected to the customer home page
   - Staff members are redirected to the admin dashboard 