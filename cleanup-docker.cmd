@echo off
echo ========================================
echo   Docker Cleanup for K8s
echo ========================================

echo.
echo Current costume-rental images:
docker images | findstr "costume-rental"

echo.
echo Removing old costume-rental images...
docker images --format "{{.Repository}}:{{.Tag}}" | findstr "costume-rental" > temp_images.txt
if exist temp_images.txt (
    for /f %%i in (temp_images.txt) do (
        echo Removing %%i...
        docker rmi %%i --force 2>nul
    )
    del temp_images.txt
)

echo.
echo Cleaning up dangling images...
docker image prune -f

echo.
echo Cleaning up unused containers...
docker container prune -f

echo.
echo ========================================
echo   Docker cleanup completed!
echo ========================================
echo.
pause
