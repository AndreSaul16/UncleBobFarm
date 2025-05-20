# Azure Cost Estimation for UncleBobFarm Project

## Introduction
This document provides a cost estimation for hosting the UncleBobFarm application on Azure. The estimation is based on the Azure Pricing Calculator and represents an approximate monthly and annual cost.

## Infrastructure Components

### Virtual Machine
- **Size**: Standard B2s (2 vCPUs, 4 GB RAM)
- **Operating System**: Linux (Ubuntu)
- **Usage**: 730 hours/month (running continuously)
- **Region**: East US
- **Estimated Monthly Cost**: $31.39
- **Estimated Annual Cost**: $376.68

### Storage
- **Type**: Standard HDD Managed Disks
- **Size**: 30 GB (OS disk)
- **Estimated Monthly Cost**: $1.54
- **Estimated Annual Cost**: $18.48

### Outbound Data Transfer
- **Estimated Usage**: 100 GB/month
- **Estimated Monthly Cost**: $8.50 (first 5 GB free, then $0.087/GB)
- **Estimated Annual Cost**: $102.00

## Optional Components (if needed)

### Azure Database for MySQL
- **Tier**: Basic
- **Compute**: 1 vCore
- **Storage**: 5 GB
- **Backup Storage**: 5 GB
- **Estimated Monthly Cost**: $25.32
- **Estimated Annual Cost**: $303.84

### Azure Container Registry (for Docker images)
- **Tier**: Basic
- **Estimated Monthly Cost**: $5.00
- **Estimated Annual Cost**: $60.00

### Application Insights (for monitoring)
- **Estimated Data Ingestion**: 5 GB/month
- **Estimated Monthly Cost**: $11.50
- **Estimated Annual Cost**: $138.00

## Total Estimated Costs

### Minimum Configuration (VM + Storage + Data Transfer)
- **Monthly**: $41.43
- **Annual**: $497.16

### Complete Configuration (including optional components)
- **Monthly**: $83.25
- **Annual**: $999.00

## Cost Optimization Options

### Reserved Instances for VM
- **1-year Reserved Instance**: ~35% savings on VM costs
- **3-year Reserved Instance**: ~60% savings on VM costs

### Automated Shutdown/Startup
- If the application doesn't need 24/7 availability, implementing automated shutdown during off-hours could reduce VM costs by 50-70%

### B-Series Burstable VMs
- The B2s VM size is already a cost-effective option that allows for bursting when needed

## Comparison with Other Cloud Providers

### AWS
- Comparable EC2 instance (t3.medium): ~$35/month
- RDS MySQL: ~$30/month
- Similar data transfer: ~$9/month
- **Total AWS Estimated Monthly Cost**: ~$74

### Google Cloud Platform
- Comparable E2 instance: ~$33/month
- Cloud SQL MySQL: ~$28/month
- Similar data transfer: ~$8/month
- **Total GCP Estimated Monthly Cost**: ~$69

## Conclusions

Azure provides a competitive pricing structure for hosting the UncleBobFarm application, particularly when using B-series VMs which are cost-effective for workloads like this farm management system.

The estimated costs assume continuous operation and moderate usage. Actual costs may vary based on:
- Real traffic patterns
- Data storage growth over time
- Any additional Azure services added later

For the current requirements, the basic configuration should be sufficient, with an estimated annual cost of approximately $500.

*Note: All prices are in USD and are based on the Azure pricing as of May 2025. Prices are subject to change.*
