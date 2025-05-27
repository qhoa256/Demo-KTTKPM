@echo off
echo ========================================
echo   Costume Rental System - Docker
echo ========================================
echo.

echo Starting Costume Rental System...
docker-compose up -d

echo.
echo Waiting for services to be ready...
timeout /t 30 /nobreak > nul

echo.
echo Checking service status...
docker-compose ps

echo.
echo ========================================
echo   System Status
echo ========================================
echo Frontend: http://localhost:8080
echo User Service: http://localhost:8081/actuator/health
echo Costume Service: http://localhost:8082/actuator/health
echo Bill Service: http://localhost:8083/actuator/health
echo Supplier Service: http://localhost:8084/actuator/health
echo Import Bill Service: http://localhost:8085/actuator/health
echo Database: localhost:3307 (root/1)
echo ========================================
echo.
echo System is ready! Press any key to exit...
pause > nul
