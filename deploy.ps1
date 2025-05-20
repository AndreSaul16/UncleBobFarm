# Deploy script for UncleBobFarm application (PowerShell)

Write-Host "==== Starting UncleBobFarm deployment ====" -ForegroundColor Green

# Build and start the containers
Write-Host "Building and starting Docker containers..." -ForegroundColor Cyan
docker-compose up -d --build

# Check if containers are running
Write-Host "Checking container status..." -ForegroundColor Cyan
docker ps -a | Select-String -Pattern "unclebob"

Write-Host "==== Deployment complete ====" -ForegroundColor Green
Write-Host "Application should be accessible at http://localhost:8081" -ForegroundColor Yellow

# Display logs for the backend (uncomment if needed)
# Write-Host "Showing backend logs (Ctrl+C to exit):" -ForegroundColor Cyan
# docker logs -f unclebob-backend
