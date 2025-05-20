# UncleBobFarm Azure Deployment Guide

## Overview

This guide provides detailed instructions for deploying the UncleBobFarm application to Microsoft Azure. The deployment uses Docker and Docker Compose to containerize the application and database.

## Prerequisites

- Microsoft Azure account
- Azure CLI installed locally
- Docker and Docker Compose installed locally (for testing)
- Git repository with the UncleBobFarm project

## Deployment Options

### Option 1: Automated Deployment (Recommended)

1. **Prepare your Azure CLI environment**

   Make sure you're logged in to Azure:
   ```powershell
   az login
   ```

2. **Run the deployment script**

   For Windows:
   ```powershell
   .\deploy-azure.ps1
   ```

   For Linux/Mac:
   ```bash
   bash deploy-azure.sh
   ```

3. **Access the VM via SSH**

   Once the script completes, it will display the IP address. Use it to connect:
   ```
   ssh azureuser@<your-vm-ip>
   ```

4. **Clone and deploy the application**

   ```bash
   git clone <your-repository-url>
   cd UncleBobFarm-dev
   sudo docker-compose up -d
   ```

### Option 2: Manual Deployment

1. **Create Azure Resources**

   - Create a Resource Group
   - Create a Virtual Network
   - Create a Network Security Group with rules for ports 22, 80, 443, and 8081
   - Create a VM (Ubuntu 22.04 LTS recommended)
   - Configure public IP and DNS name

2. **Set up the VM**

   - Install Docker and Docker Compose:
     ```bash
     # Install Docker
     sudo apt update
     sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
     curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
     echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
     sudo apt update
     sudo apt install -y docker-ce docker-ce-cli containerd.io
     sudo systemctl enable docker
     sudo systemctl start docker
     
     # Install Docker Compose
     sudo curl -L "https://github.com/docker/compose/releases/download/v2.23.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
     sudo chmod +x /usr/local/bin/docker-compose
     sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
     
     # Add your user to the docker group
     sudo usermod -aG docker ${USER}
     ```

   - Deploy the application:
     ```bash
     git clone <your-repository-url>
     cd UncleBobFarm-dev
     docker-compose up -d
     ```

## Verification and Monitoring

### Verify Deployment

1. **Check the container status**
   ```bash
   docker ps
   ```

2. **Check the logs**
   ```bash
   docker-compose logs -f
   ```

3. **Access the application**
   Open a web browser and navigate to:
   ```
   http://<your-vm-ip>:8081
   ```

### Monitoring

1. **Container Monitoring**
   ```bash
   docker stats
   ```

2. **Application Health Check**
   ```bash
   curl http://localhost:8081/actuator/health
   ```

## Adding SSL (HTTPS) Support

1. **Install Nginx**
   ```bash
   sudo apt install -y nginx
   ```

2. **Configure Nginx as a reverse proxy**
   Create a new file at `/etc/nginx/sites-available/unclebobfarm`:
   ```
   server {
       listen 80;
       server_name <your-domain-or-ip>;
       
       location / {
           proxy_pass http://localhost:8081;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
       }
   }
   ```

3. **Enable the site**
   ```bash
   sudo ln -s /etc/nginx/sites-available/unclebobfarm /etc/nginx/sites-enabled/
   sudo nginx -t
   sudo systemctl restart nginx
   ```

4. **Set up SSL with Certbot**
   ```bash
   sudo apt install -y certbot python3-certbot-nginx
   sudo certbot --nginx -d <your-domain>
   ```

## Troubleshooting

### Database Connection Issues

If the application can't connect to the database:

1. Verify both containers are running:
   ```bash
   docker ps
   ```

2. Check MySQL container logs:
   ```bash
   docker logs unclebob-mysql
   ```

3. Verify the connection settings in `application-docker.properties`

### Application Startup Issues

1. Check application logs:
   ```bash
   docker logs unclebob-backend
   ```

2. Verify the Docker environment variables:
   ```bash
   docker-compose config
   ```

## Maintenance

### Backup Database

```bash
docker exec unclebob-mysql sh -c 'exec mysqldump -uroot -p"root" --all-databases' > backup.sql
```

### Update Application

1. Pull latest changes:
   ```bash
   git pull
   ```

2. Rebuild and restart:
   ```bash
   docker-compose up -d --build
   ```

### Container Management

1. Stop all containers:
   ```bash
   docker-compose down
   ```

2. Remove all containers including volumes:
   ```bash
   docker-compose down -v
   ```

## Security Considerations

1. **Secure MySQL**
   - Use strong passwords
   - Restrict access to the database port
   - Regularly update credentials

2. **Application Security**
   - Use HTTPS with valid SSL certificates
   - Implement rate limiting
   - Configure proper authentication

3. **Server Security**
   - Keep the system updated
   - Use a firewall (Azure NSG or ufw)
   - Implement regular security audits

## Additional Resources

- [Azure Documentation](https://docs.microsoft.com/en-us/azure/)
- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

## Cost Optimization

See the AZURE_COST_ESTIMATION.md file for detailed cost estimates and optimization strategies.
