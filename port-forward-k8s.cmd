@echo off
echo ========================================
echo   Setting up Port Forwarding
echo ========================================

echo.
echo Starting port forwarding for client application...
echo Access the application at: http://localhost:8080
echo.
echo Press Ctrl+C to stop port forwarding
echo.

kubectl port-forward svc/client-costume-rental 8080:8080 -n costume-rental
