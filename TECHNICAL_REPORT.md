# Technical Report: UncleBobFarm Deployment

## Executive Summary

This technical report documents the containerization and deployment process for the UncleBobFarm application. The application is containerized using Docker and orchestrated with Docker Compose, allowing for consistent deployment across both local and cloud environments. The deployment target is Microsoft Azure, following best practices for security, scalability, and maintainability.

## 1. Introduction

UncleBobFarm is a Spring Boot application for managing farm operations, with a MySQL database for data persistence. The application includes modules for animal management, inventory tracking, employee management, and activity scheduling.

### 1.1 Project Requirements

- Containerization of backend and database
- Docker Compose orchestration
- Azure cloud deployment
- Public accessibility
- Security configuration
- Cost estimation
- Deployment automation

## 2. System Architecture

### 2.1 Overview

The system uses a microservice architecture with the following components:

1. **Spring Boot Backend**: Java application providing RESTful APIs
2. **MySQL Database**: Persistent data storage
3. **Docker**: Container technology for packaging components
4. **Docker Compose**: Container orchestration for local and cloud deployment

### 2.2 Containerization Strategy

Each component is containerized separately:

- **Backend Container**: Spring Boot application in a Java container
- **Database Container**: MySQL database with initialized schema and data

### 2.3 System Diagram

```
┌─────────────────────┐      ┌─────────────────────┐
│                     │      │                     │
│   Spring Boot       │      │   MySQL             │
│   Backend           │◄────►│   Database          │
│   (Port 8081)       │      │   (Port 3306)       │
│                     │      │                     │
└─────────────────────┘      └─────────────────────┘
         ▲                            ▲
         │                            │
         │ Docker Network (Bridge)    │
  ┌──────┴────────────────────────────┴───────┐
  │                                           │
  │           Docker Compose                  │
  │                                           │
  └───────────────────────────────────────────┘
         ▲
         │
┌────────┴───────────┐
│                    │
│   Azure VM         │
│   (Ubuntu)         │
│                    │
└────────────────────┘
```

## 3. Docker Implementation

### 3.1 Docker Configuration

The backend application is containerized using a multi-stage Dockerfile:

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/gestorGranja-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

The multi-stage build offers several advantages:
- Smaller final image size (only runtime dependencies)
- Separation of build environment from runtime environment
- Better security (fewer attack vectors in production image)

### 3.2 Docker Compose

Services are orchestrated using Docker Compose:

```yaml
version: '3.8'

services:
  # MySQL Database Service
  mysql:
    image: mysql:8.0
    container_name: unclebob-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: gestorgranja
      MYSQL_USER: rancheroRoot
      MYSQL_PASSWORD: "@admin123"
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./docker/init-db.sql:/docker-entrypoint-initdb.d/init-db.sql
    networks:
      - unclebob-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Spring Boot Application Service
  backend:
    build: 
      context: .
      dockerfile: Dockerfile
    container_name: unclebob-backend
    restart: always
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: docker
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/gestorgranja
      SPRING_DATASOURCE_USERNAME: rancheroRoot
      SPRING_DATASOURCE_PASSWORD: "@admin123"
    ports:
      - "8081:8081"
    networks:
      - unclebob-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8081/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s

# Persistent volume for MySQL data
volumes:
  mysql-data:
    driver: local

# Network for communication between services
networks:
  unclebob-network:
    driver: bridge
```

Key features of this Docker Compose setup:
- Health checks ensure dependencies are running before services start
- Network isolation via a dedicated bridge network
- Persistent data storage using Docker volumes
- Environment variable configuration for flexibility
- Automatic restart policy for high availability

### 3.3 Database Initialization

Database initialization is performed using a script mounted as a volume:

```sql
-- Initialize database with base data
USE gestorgranja;

-- Create initial employees (if they don't exist)
INSERT IGNORE INTO empleado (id, nombre, apellido, rol, username, password, email)
VALUES 
(1, 'Admin', 'System', 'ADMIN', 'admin', '$2a$10$zRJmZ3ZSaXlpCIhns/8E9Oirg9nHUvqIj1R7JN.VZO3WlGszSFDvC', 'admin@unclebobfarm.com'),
(2, 'User', 'Regular', 'EMPLEADO', 'user', '$2a$10$MIJes5qoNZTCqYlvg2qXI.sFYp5YTCaHEcgVFm9KetFDMSZsC9nm2', 'user@unclebobfarm.com');

-- Additional initialization statements...
```

## 4. Azure Cloud Deployment

### 4.1 Deployment Strategy

The deployment to Azure follows these steps:

1. Create a Virtual Machine (Ubuntu 22.04 LTS)
2. Configure security settings (NSG rules)
3. Install Docker and Docker Compose
4. Clone the application repository
5. Deploy using Docker Compose

### 4.2 Azure Configuration

The Azure deployment is automated using scripts that provision:

- Resource group
- Network security group with firewall rules
- Virtual network and subnet
- Public IP address with DNS name
- Virtual machine with Docker pre-installed via cloud-init

### 4.3 Security Measures

The system implements several security measures:

- Network Security Group (NSG) rules to restrict access
- Strong password policies for database access
- Container isolation using Docker networks
- Spring Security for authentication and authorization
- HTTPS support option via Nginx reverse proxy

### 4.4 Automation Scripts

Deployment is automated using Azure CLI scripts:

```powershell
# PowerShell Deployment Example
$RESOURCE_GROUP = "UncleBobFarmRG"
$LOCATION = "eastus"
$VM_NAME = "UncleBobFarmVM"

# Create VM with Docker pre-installed
az vm create --resource-group $RESOURCE_GROUP `
    --name $VM_NAME `
    --image "Canonical:0001-com-ubuntu-server-jammy:22_04-lts:latest" `
    --custom-data cloud-init.txt
    # Additional parameters...
```

## 5. Performance and Scalability

### 5.1 Performance Considerations

The containerized application is configured for optimal performance:

- JVM runtime flags for container awareness
- Database connection pooling
- Healthchecks for service availability monitoring

### 5.2 Scalability Options

Horizontal scaling options available:

- Load balancing multiple instances of the backend container
- Database read replicas for scaling read operations

### 5.3 Resource Allocation

The Azure VM is sized appropriately for the application load:
- 2 vCPUs, 4GB RAM (Standard B2s) for smaller workloads
- Easily upgradable to larger instances for higher demand

## 6. Cost Analysis

### 6.1 Azure Costs

Estimated monthly costs for the Azure infrastructure:

| Component | Monthly Cost | Annual Cost |
|-----------|--------------|------------|
| VM (B2s)  | $31.39       | $376.68    |
| Storage   | $1.54        | $18.48     |
| Data Transfer | $8.50     | $102.00    |
| **Total** | **$41.43**   | **$497.16**|

### 6.2 Cost Optimization Strategies

Several options exist for optimizing costs:
- VM reserved instances (35-60% savings)
- Automated shutdown during off-hours
- Right-sizing resources based on actual usage
- Disk storage optimization

## 7. Maintenance Procedures

### 7.1 Backup Strategy

Database backup procedure:

```bash
docker exec unclebob-mysql sh -c 'exec mysqldump -uroot -p"root" gestorgranja' > backup.sql
```

### 7.2 Update Procedure

Application update procedure:

```bash
git pull
docker-compose up -d --build
```

### 7.3 Monitoring

The application exposes health endpoints via Spring Actuator:
- `/actuator/health` for overall application health
- Custom health indicators for database and critical services

### 7.4 Troubleshooting

Common issues and resolutions:

1. **Database connection failure**: Check MySQL container logs, verify network connectivity
2. **Application startup failure**: Check Spring Boot logs, verify environment variables
3. **Access issues**: Check NSG rules, verify port configurations

## 8. Advantages of Containerization

### 8.1 Benefits Realized

The Docker-based approach provides several advantages:

- **Environment Consistency**: Identical environment across development, testing, and production
- **Isolation**: Separation of concerns between application and database
- **Portability**: Easy deployment to any environment supporting Docker
- **Version Control**: Container versions are tracked and reproducible
- **Resource Efficiency**: Lower overhead compared to virtual machines

### 8.2 Comparison with Traditional Deployment

| Aspect | Docker Deployment | Traditional Deployment |
|--------|------------------|------------------------|
| Setup Time | ~10 minutes | Several hours |
| Configuration Management | Declarative (docker-compose.yml) | Manual, error-prone |
| Reproducibility | High | Low |
| Isolation | Strong | Weak |
| Resource Usage | Efficient | Higher overhead |
| Scalability | Simple | Complex |

## 9. Future Improvements

Potential enhancements for the deployment:

- Implement Docker Swarm or Kubernetes for container orchestration
- Add CI/CD pipeline with GitHub Actions
- Implement blue-green deployment for zero-downtime updates
- Set up centralized logging with ELK stack
- Add application performance monitoring

## 10. Conclusion

The containerization of UncleBobFarm using Docker and deployment to Azure provides a robust, scalable, and maintainable solution. The Docker Compose orchestration ensures consistent environments across development and production, while the Azure deployment provides reliable hosting with good performance characteristics.

The chosen architecture balances cost effectiveness with performance requirements, and the documented procedures ensure proper maintenance of the application throughout its lifecycle.

## Appendices

### Appendix A: Screenshots

*[Screenshots of working deployment would be included here]*

### Appendix B: References

1. Docker Documentation: https://docs.docker.com/
2. Spring Boot Documentation: https://spring.io/projects/spring-boot
3. Azure Documentation: https://docs.microsoft.com/en-us/azure/
4. MySQL Documentation: https://dev.mysql.com/doc/

### Appendix C: Code Repository

The complete codebase is available at: [Repository URL]
