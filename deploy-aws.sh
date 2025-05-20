#!/bin/bash
# AWS Deployment Script for UncleBobFarm

# Exit immediately if a command exits with a non-zero status
set -e

echo "===== Starting AWS deployment for UncleBobFarm ====="

# Check if AWS CLI is installed
if ! command -v aws &> /dev/null; then
    echo "AWS CLI is not installed. Please install it first."
    exit 1
fi

# Variables - modify these as needed
EC2_INSTANCE_TYPE="t2.micro"
KEY_NAME="unclebob-key"
SECURITY_GROUP_NAME="unclebob-sg"
AMI_ID="ami-0c55b159cbfafe1f0"  # Ubuntu 20.04 LTS, change according to your region
REGION="us-east-1"

# Create security group (if it doesn't exist)
echo "Creating security group..."
aws ec2 create-security-group \
    --group-name $SECURITY_GROUP_NAME \
    --description "Security group for UncleBobFarm application" \
    --region $REGION || true

# Add inbound rules for SSH, HTTP, and application port
echo "Adding security group rules..."
aws ec2 authorize-security-group-ingress \
    --group-name $SECURITY_GROUP_NAME \
    --protocol tcp \
    --port 22 \
    --cidr 0.0.0.0/0 \
    --region $REGION || true

aws ec2 authorize-security-group-ingress \
    --group-name $SECURITY_GROUP_NAME \
    --protocol tcp \
    --port 80 \
    --cidr 0.0.0.0/0 \
    --region $REGION || true

aws ec2 authorize-security-group-ingress \
    --group-name $SECURITY_GROUP_NAME \
    --protocol tcp \
    --port 8081 \
    --cidr 0.0.0.0/0 \
    --region $REGION || true

# Launch EC2 instance
echo "Launching EC2 instance..."
INSTANCE_ID=$(aws ec2 run-instances \
    --image-id $AMI_ID \
    --count 1 \
    --instance-type $EC2_INSTANCE_TYPE \
    --key-name $KEY_NAME \
    --security-groups $SECURITY_GROUP_NAME \
    --region $REGION \
    --query 'Instances[0].InstanceId' \
    --output text)

echo "Waiting for instance $INSTANCE_ID to be running..."
aws ec2 wait instance-running --instance-ids $INSTANCE_ID --region $REGION

# Get instance public IP
PUBLIC_IP=$(aws ec2 describe-instances \
    --instance-ids $INSTANCE_ID \
    --query 'Reservations[0].Instances[0].PublicIpAddress' \
    --output text \
    --region $REGION)

echo "Instance is running with public IP: $PUBLIC_IP"

# Wait for SSH to be available
echo "Waiting for SSH to be available..."
while ! ssh -o StrictHostKeyChecking=no -i "$KEY_NAME.pem" ubuntu@$PUBLIC_IP echo "SSH is up"; do
    sleep 5
done

# Install Docker and Docker Compose
echo "Installing Docker and Docker Compose..."
ssh -i "$KEY_NAME.pem" ubuntu@$PUBLIC_IP << 'EOF'
    sudo apt-get update
    sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    sudo apt-get update
    sudo apt-get install -y docker-ce
    sudo systemctl enable docker
    sudo systemctl start docker
    sudo usermod -aG docker ubuntu
    sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
EOF

# Clone the repository
echo "Cloning repository..."
ssh -i "$KEY_NAME.pem" ubuntu@$PUBLIC_IP << 'EOF'
    git clone <YOUR_REPOSITORY_URL> UncleBobFarm-dev
    cd UncleBobFarm-dev
    # Start the application
    docker-compose up -d
EOF

echo "===== Deployment completed successfully ====="
echo "Application is running at: http://$PUBLIC_IP:8081"
echo "SSH access: ssh -i $KEY_NAME.pem ubuntu@$PUBLIC_IP"
