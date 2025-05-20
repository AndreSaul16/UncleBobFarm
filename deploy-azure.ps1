# Deploy script for UncleBobFarm application on Azure
# This script assumes you have the Azure CLI installed and are logged in

# Configuration variables
$RESOURCE_GROUP = "UncleBobFarmRG"
$LOCATION = "eastus"
$VM_NAME = "UncleBobFarmVM"
$VM_SIZE = "Standard_B2s"
$VM_IMAGE = "Canonical:0001-com-ubuntu-server-jammy:22_04-lts:latest"
$NSG_NAME = "UncleBobFarmNSG"
$PUBLIC_IP_NAME = "UncleBobFarmPublicIP"

Write-Host "==== Starting UncleBobFarm Azure deployment ====" -ForegroundColor Green

# Create resource group if it doesn't exist
Write-Host "Creating Resource Group..." -ForegroundColor Cyan
az group create --name $RESOURCE_GROUP --location $LOCATION

# Create a network security group
Write-Host "Creating Network Security Group..." -ForegroundColor Cyan
az network nsg create --resource-group $RESOURCE_GROUP --name $NSG_NAME

# Create NSG rules
Write-Host "Adding NSG rules..." -ForegroundColor Cyan
az network nsg rule create --resource-group $RESOURCE_GROUP --nsg-name $NSG_NAME `
    --name SSH --priority 100 --protocol Tcp --destination-port-range 22 `
    --access Allow

az network nsg rule create --resource-group $RESOURCE_GROUP --nsg-name $NSG_NAME `
    --name HTTP --priority 110 --protocol Tcp --destination-port-range 80 `
    --access Allow

az network nsg rule create --resource-group $RESOURCE_GROUP --nsg-name $NSG_NAME `
    --name HTTPS --priority 120 --protocol Tcp --destination-port-range 443 `
    --access Allow

az network nsg rule create --resource-group $RESOURCE_GROUP --nsg-name $NSG_NAME `
    --name AppPort --priority 130 --protocol Tcp --destination-port-range 8081 `
    --access Allow

# Create a public IP address
Write-Host "Creating Public IP address..." -ForegroundColor Cyan
az network public-ip create --resource-group $RESOURCE_GROUP `
    --name $PUBLIC_IP_NAME --allocation-method Static `
    --dns-name unclebobfarm

# Create a virtual network
Write-Host "Creating Virtual Network..." -ForegroundColor Cyan
az network vnet create --resource-group $RESOURCE_GROUP `
    --name UncleBobFarmVNet --address-prefix 10.0.0.0/16 `
    --subnet-name default --subnet-prefix 10.0.0.0/24 `
    --network-security-group $NSG_NAME

# Create the VM
Write-Host "Creating Virtual Machine..." -ForegroundColor Cyan
az vm create --resource-group $RESOURCE_GROUP `
    --name $VM_NAME `
    --image $VM_IMAGE `
    --admin-username azureuser `
    --generate-ssh-keys `
    --nsg $NSG_NAME `
    --public-ip-address $PUBLIC_IP_NAME `
    --size $VM_SIZE `
    --custom-data cloud-init.txt

# Get the public IP address
$PUBLIC_IP = az network public-ip show --resource-group $RESOURCE_GROUP `
    --name $PUBLIC_IP_NAME --query ipAddress -o tsv

Write-Host "==== VM Created with IP: $PUBLIC_IP ====" -ForegroundColor Green
Write-Host "Website will be available at http://$PUBLIC_IP`:8081 once setup completes" -ForegroundColor Yellow
Write-Host "SSH access: ssh azureuser@$PUBLIC_IP" -ForegroundColor Cyan
Write-Host ""
Write-Host "To deploy the application to this VM:" -ForegroundColor Magenta
Write-Host "1. SSH into the VM: ssh azureuser@$PUBLIC_IP" -ForegroundColor White
Write-Host "2. Clone the repository" -ForegroundColor White
Write-Host "3. Navigate to the project directory" -ForegroundColor White
Write-Host "4. Run: sudo bash deploy.sh" -ForegroundColor White

Write-Host "==== Deployment script completed ====" -ForegroundColor Green
