#!/bin/bash

# Deploy script for UncleBobFarm application

echo "==== Starting UncleBobFarm deployment ===="

# Build and start the containers
echo "Building and starting Docker containers..."
docker-compose up -d --build

# Check if containers are running
echo "Checking container status..."
docker ps -a | grep unclebob

echo "==== Deployment complete ===="
echo "Application should be accessible at http://localhost:8081"

# Display logs for the backend (uncomment if needed)
# echo "Showing backend logs (Ctrl+C to exit):"
# docker logs -f unclebob-backend
