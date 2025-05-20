# UncleBobFarm Docker Deployment Guide

## Description
This document provides information on deploying the UncleBobFarm application using Docker and Docker Compose.

## Components
- **Backend**: Spring Boot application (Java 17)
- **Database**: MySQL 8.0

## Prerequisites
- Docker and Docker Compose installed
- Git (to clone the repository if needed)

## Local Deployment Steps

### 1. Clone the Repository (if needed)
```bash
git clone <repository-url>
cd UncleBobFarm-dev
```

### 2. Start the Application
Using Docker Compose:
```bash
# Build and start containers
docker-compose up -d

# To view logs
docker-compose logs -f
```

Or use the provided deployment scripts:
- On Windows: `.\deploy.ps1`
- On Linux/Mac: `bash deploy.sh`

### 3. Access the Application
- The application will be available at http://localhost:8081

## Container Management

### View Running Containers
```bash
docker ps
```

### Stop Containers
```bash
docker-compose down
```

### Stop and Remove All Data (including volumes)
```bash
docker-compose down -v
```

## Troubleshooting

### Database Connectivity Issues
If the application cannot connect to the database:
1. Ensure MySQL container is running: `docker ps | grep mysql`
2. Check MySQL logs: `docker logs unclebob-mysql`
3. Verify network connectivity between containers:
   ```bash
   docker exec -it unclebob-backend ping mysql
   ```

### Application Won't Start
1. Check application logs: `docker logs unclebob-backend`
2. Ensure correct environment variables are set in docker-compose.yml
3. Rebuild the application: `docker-compose up -d --build backend`

## Cloud Deployment

For deploying to cloud platforms:

### AWS EC2
1. Launch an EC2 instance (Ubuntu recommended)
2. Install Docker and Docker Compose
3. Clone the repository and run docker-compose up

### Azure VM
1. Create an Azure VM (Ubuntu recommended)
2. Install Docker and Docker Compose
3. Clone the repository and run docker-compose up

## Security Considerations
- Credential management: In production, avoid hardcoding passwords in docker-compose.yml
- Use Docker secrets or environment variables
- Set up proper network rules to limit access to only required ports

## Maintenance

### Database Backup
```bash
docker exec unclebob-mysql sh -c 'exec mysqldump -uroot -p"root" gestorgranja' > backup.sql
```

### Database Restore
```bash
cat backup.sql | docker exec -i unclebob-mysql sh -c 'exec mysql -uroot -p"root" gestorgranja'
```

## Additional Information
- Spring Boot application runs on port 8081
- MySQL runs on port 3306
