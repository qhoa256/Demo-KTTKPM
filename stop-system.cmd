@echo off
echo ========================================
echo   Costume Rental System - Docker
echo ========================================
echo.

echo Stopping Costume Rental System...
docker-compose down

echo.
echo System stopped successfully!
echo.
echo To start again, run: start-system.cmd
echo ========================================
echo.
pause
