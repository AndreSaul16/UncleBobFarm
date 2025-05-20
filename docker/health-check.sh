#!/bin/bash

# Health check script for UncleBobFarm application

if [ "$1" == "local" ]; then
  URL="http://localhost:8081/actuator/health"
elif [ "$1" == "docker" ]; then
  URL="http://backend:8081/actuator/health"
else
  echo "Usage: $0 [local|docker]"
  exit 1
fi

echo "Checking health at $URL..."

# Try to access the health endpoint
STATUS=$(curl -s -o /dev/null -w "%{http_code}" $URL)

if [ $STATUS -eq 200 ]; then
  echo "Application is healthy (HTTP $STATUS)"
  exit 0
else
  echo "Application is not healthy (HTTP $STATUS)"
  exit 1
fi
