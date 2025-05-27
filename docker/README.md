# Docker Configuration for Costume Rental System

This directory contains Docker configuration files and scripts for the Costume Rental System microservices.

## Overview

The system consists of 6 microservices:
- **user-service** (Port 8081) - User management
- **costume-service** (Port 8082) - Costume catalog management
- **bill-costume-service** (Port 8083) - Rental bill management
- **supplier-service** (Port 8084) - Supplier management
- **import-bill-service** (Port 8085) - Import bill management
- **client-costume-rental** (Port 8080) - Frontend application

## Prerequisites

- Docker Desktop installed and running
- PowerShell (for Windows scripts)
- At least 4GB RAM available for Docker
- Ports 8080-8085 and 3306 available

## Quick Start

### 1. Build All Images
```powershell
.\docker\build.ps1
```

### 2. Deploy Production Environment
```powershell
.\docker\deploy.ps1
```

### 3. Deploy Development Environment (with phpMyAdmin)
```powershell
.\docker\deploy.ps1 -Environment dev
```

### 4. Access the Application
- Frontend: http://localhost:8080
- phpMyAdmin (dev only): http://localhost:8090

## Available Scripts

### build.ps1
Builds Docker images for all microservices.

**Parameters:**
- `-Tag` - Docker image tag (default: "latest")
- `-NoBuildCache` - Build without using cache

**Examples:**
```powershell
# Build with default settings
.\docker\build.ps1

# Build with custom tag
.\docker\build.ps1 -Tag "v1.0.0"

# Build without cache
.\docker\build.ps1 -NoBuildCache
```

### deploy.ps1
Deploys the entire system using Docker Compose.

**Parameters:**
- `-Environment` - Environment type: "prod" or "dev" (default: "prod")
- `-Build` - Build images before deploying
- `-Detached` - Run in detached mode (default: true)

**Examples:**
```powershell
# Deploy production environment
.\docker\deploy.ps1

# Deploy development environment with build
.\docker\deploy.ps1 -Environment dev -Build

# Deploy in foreground mode
.\docker\deploy.ps1 -Detached:$false
```

### cleanup.ps1
Cleans up Docker resources.

**Parameters:**
- `-Environment` - Environment to cleanup: "prod" or "dev" (default: "prod")
- `-RemoveVolumes` - Remove data volumes (⚠️ destroys data)
- `-RemoveImages` - Remove Docker images
- `-Force` - Skip confirmation prompt

**Examples:**
```powershell
# Basic cleanup (stops containers)
.\docker\cleanup.ps1

# Full cleanup including volumes and images
.\docker\cleanup.ps1 -RemoveVolumes -RemoveImages -Force

# Cleanup development environment
.\docker\cleanup.ps1 -Environment dev
```

## Docker Compose Files

### docker-compose.yml
Production environment configuration:
- All microservices
- MySQL database
- Health checks
- Service dependencies
- Production-optimized settings

### docker-compose.dev.yml
Development environment configuration:
- All microservices with volume mounts for hot reload
- MySQL database on port 3307
- phpMyAdmin for database management
- Development-optimized settings

## Database Configuration

### MySQL Container
- **Image:** mysql:8.0
- **Root Password:** 1
- **Port:** 3306 (prod) / 3307 (dev)
- **Databases:** Automatically created for each service

### Database Initialization
The `mysql/init/01-init-databases.sql` script automatically creates:
- user_service_db
- costume_service_db
- bill_costume_service_db
- supplier_service_db
- import_bill_service_db

## Service Configuration

Each microservice has two configuration files:
- `application.properties` - Local development
- `application-docker.properties` - Docker environment

### Key Docker Configuration Changes:
- Database host: `mysql-db` instead of `localhost`
- Service URLs: Use container names instead of localhost
- Database password: `1` (matching Docker environment)

## Health Checks

All services include health checks:
- **Database:** MySQL ping test
- **Services:** Spring Boot Actuator health endpoint
- **Intervals:** 30s with 60s startup period
- **Retries:** 5 attempts with 10s timeout

## Networking

All services run on a custom bridge network:
- **Production:** `costume-rental-network`
- **Development:** `costume-rental-dev-network`

This enables:
- Service-to-service communication using container names
- Isolation from other Docker applications
- DNS resolution between containers

## Volumes

### Production
- `mysql_data` - MySQL data persistence

### Development
- `mysql_dev_data` - MySQL data persistence
- Source code volumes for hot reload

## Troubleshooting

### Common Issues

1. **Port conflicts**
   ```powershell
   # Check what's using the ports
   netstat -ano | findstr :8080
   ```

2. **Database connection issues**
   ```powershell
   # Check MySQL container logs
   docker-compose logs mysql-db
   ```

3. **Service startup failures**
   ```powershell
   # Check service logs
   docker-compose logs [service-name]
   ```

4. **Build failures**
   ```powershell
   # Clean build without cache
   .\docker\build.ps1 -NoBuildCache
   ```

### Useful Commands

```powershell
# View all containers
docker-compose ps

# View logs for all services
docker-compose logs -f

# View logs for specific service
docker-compose logs -f user-service

# Restart a specific service
docker-compose restart user-service

# Scale a service (if needed)
docker-compose up -d --scale user-service=2

# Execute command in container
docker-compose exec user-service bash

# Check resource usage
docker stats
```

## Performance Considerations

- **Memory:** Each service requires ~512MB RAM
- **CPU:** Multi-core recommended for parallel builds
- **Storage:** ~2GB for images + database data
- **Network:** Services communicate internally via Docker network

## Security Notes

- Database password is simplified for development
- Services run as non-root users in containers
- No sensitive data in environment variables
- Health checks use internal endpoints only
