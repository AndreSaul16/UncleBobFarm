#!/bin/bash

# Deploy script for UncleBobFarm application on Azure
# This script assumes you have the Azure CLI installed and are logged in

# Configuration variables
RESOURCE_GROUP="UncleBobFarmRG"
LOCATION="eastus"
VM_NAME="UncleBobFarmVM"
VM_SIZE="Standard_B2s"
VM_IMAGE="Canonical:0001-com-ubuntu-server-jammy:22_04-lts:latest"
NSG_NAME="UncleBobFarmNSG"
PUBLIC_IP_NAME="UncleBobFarmPublicIP"

echo "==== Starting UncleBobFarm Azure deployment ===="

# Create resource group if it doesn't exist
echo "Creating Resource Group..."
az group create --name $RESOURCE_GROUP --location $LOCATION

# Create a network security group
echo "Creating Network Security Group..."
az network nsg create --resource-group $RESOURCE_GROUP --name $NSG_NAME

# Create NSG rules
echo "Adding NSG rules..."
az network nsg rule create --resource-group $RESOURCE_GROUP --nsg-name $NSG_NAME \
    --name SSH --priority 100 --protocol Tcp --destination-port-range 22 \
    --access Allow

az network nsg rule create --resource-group $RESOURCE_GROUP --nsg-name $NSG_NAME \
    --name HTTP --priority 110 --protocol Tcp --destination-port-range 80 \
    --access Allow

az network nsg rule create --resource-group $RESOURCE_GROUP --nsg-name $NSG_NAME \
    --name HTTPS --priority 120 --protocol Tcp --destination-port-range 443 \
    --access Allow

az network nsg rule create --resource-group $RESOURCE_GROUP --nsg-name $NSG_NAME \
    --name AppPort --priority 130 --protocol Tcp --destination-port-range 8081 \
    --access Allow

# Create a public IP address
echo "Creating Public IP address..."
az network public-ip create --resource-group $RESOURCE_GROUP \
    --name $PUBLIC_IP_NAME --allocation-method Static \
    --dns-name unclebobfarm

# Create a virtual network
echo "Creating Virtual Network..."
az network vnet create --resource-group $RESOURCE_GROUP \
    --name UncleBobFarmVNet --address-prefix 10.0.0.0/16 \
    --subnet-name default --subnet-prefix 10.0.0.0/24 \
    --network-security-group $NSG_NAME

# Create the VM
echo "Creating Virtual Machine..."
az vm create --resource-group $RESOURCE_GROUP \
    --name $VM_NAME \
    --image $VM_IMAGE \
    --admin-username azureuser \
    --generate-ssh-keys \
    --nsg $NSG_NAME \
    --public-ip-address $PUBLIC_IP_NAME \
    --size $VM_SIZE \
    --custom-data cloud-init.txt

# Get the public IP address
PUBLIC_IP=$(az network public-ip show --resource-group $RESOURCE_GROUP \
    --name $PUBLIC_IP_NAME --query ipAddress -o tsv)

echo "==== VM Created with IP: $PUBLIC_IP ===="
echo "Website will be available at http://$PUBLIC_IP:8081 once setup completes"
echo "SSH access: ssh azureuser@$PUBLIC_IP"
echo ""
echo "To deploy the application to this VM:"
echo "1. SSH into the VM: ssh azureuser@$PUBLIC_IP"
echo "2. Clone the repository"
echo "3. Navigate to the project directory"
echo "4. Run: sudo bash deploy.sh"

echo "==== Deployment script completed ===="
