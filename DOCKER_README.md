# UncleBobFarm Docker Setup

## Overview
This project is containerized using Docker to ensure consistent environments for development, testing, and production. It consists of two main services:

1. **MySQL Database**: Stores application data
2. **Spring Boot Backend**: Provides the REST API and business logic

## Quick Start

### Prerequisites
- Docker installed (https://docs.docker.com/get-docker/)
- Docker Compose installed (https://docs.docker.com/compose/install/)

### Running the Application
From the project root directory:

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f
```

Or use the deployment scripts:
- Windows: `.\deploy.ps1`
- Linux/Mac: `bash deploy.sh`

The application will be available at http://localhost:8081

## Docker Architecture

### Services
- **mysql**: MySQL 8.0 database
  - Ports: 3306
  - Credentials: Set in docker-compose.yml
  - Data persistence: Via Docker volume
  
- **backend**: Java Spring Boot application
  - Ports: 8081
  - Built from Dockerfile
  - Connects to MySQL container via internal Docker network

### Networks
- **unclebob-network**: Internal bridge network for service communication

### Volumes
- **mysql-data**: Persistent volume for database storage

## Development Workflow

### Making Code Changes
1. Edit the source code
2. Rebuild and restart the backend container:
```bash
docker-compose up -d --build backend
```

### Database Management
- Access MySQL database:
```bash
docker exec -it unclebob-mysql mysql -urancheroRoot -p"@admin123" gestorgranja
```

### Cleanup
```bash
# Stop all containers
docker-compose down

# Stop and remove volumes (will delete database data)
docker-compose down -v
```

## Cloud Deployment
This Docker setup can be easily deployed to cloud platforms such as AWS or Azure. See the DOCKER_GUIDE.md file for detailed instructions on cloud deployment.
