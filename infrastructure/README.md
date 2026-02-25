# Infrastructure Module Documentation

## Overview
The Infrastructure module contains scripts and configuration for deploying and managing the microservices environment, both locally and in the cloud.

## Code Design & Deployment Flow
- **Deployment Scripts:**
	- `localstack-deploy.sh` automates local environment setup using LocalStack for AWS service emulation.
- **Configuration:**
	- Java code (if present) supports infrastructure automation or integration.
	- Resource and environment settings are managed in `src/main/resources/`.
- **Testing:**
	- Test utilities for infrastructure code are in `src/test/java/`.

## Deployment Process
1. **Local Deployment:**
		- Run `localstack-deploy.sh` to start local AWS-like services.
2. **Cloud Deployment:**
		- Use provided scripts or CI/CD pipelines for cloud setup.
3. **Configuration:**
		- Adjust environment variables and resource files as needed.

## Infrastructure Architecture Diagram

```mermaid
flowchart TB
    subgraph AWS_Cloud["☁️ AWS Cloud"]
        subgraph VPC["PatientManagementVPC (2 AZs)"]
            subgraph ECS["ECS Cluster"]
                APIGateway["🌐 API Gateway<br/>Fargate, ALB<br/>Port 4004"]
                Auth["🔐 Auth Service<br/>Fargate<br/>Port 4005"]
                Patient["👤 Patient Service<br/>Fargate<br/>Port 4000"]
                Billing["💳 Billing Service<br/>Fargate<br/>Port 4001, 9001"]
                Analytics["📊 Analytics Service<br/>Fargate<br/>Port 4002"]
            end
            
            subgraph RDS["RDS PostgreSQL 17.2"]
                AuthDB[("auth-service-db<br/>t2.micro, 20GB")]
                PatientDB[("patient-service-db<br/>t2.micro, 20GB")]
            end
            
            subgraph MSK["MSK"]
                Kafka[["kafka-cluster<br/>Kafka 3.6.0<br/>2 Brokers"]]
            end
        end
    end
    
    Auth --> AuthDB
    Patient --> PatientDB
    Patient --> Kafka
    Billing --> Kafka
    Analytics --> Kafka
    Patient -->|gRPC| Billing
    APIGateway --> Auth
    APIGateway --> Patient
    APIGateway --> Billing
    APIGateway --> Analytics
    
    style ECS fill:#D0FFD0,stroke:#333
    style RDS fill:#FFF8DC,stroke:#333
    style MSK fill:#FFE4E1,stroke:#333
```

```mermaid
flowchart LR
    subgraph LocalStack["🖥️ LocalStack"]
        CDK["AWS CDK App"]
        CDK -->|synth| Stack["LocalStack"]
    end
    
    subgraph Resources["Provisioned Resources"]
        VPC["VPC (2 AZs)"]
        ECS["ECS Cluster"]
        RDS["RDS Instances"]
        MSK["MSK Cluster"]
        ALB["Application LB"]
    end
    
    Stack --> VPC
    Stack --> ECS
    Stack --> RDS
    Stack --> MSK
    Stack --> ALB
```

## Source Structure
- `src/main/java/`: Infrastructure automation logic (if present).
- `src/main/resources/`: Configuration files.
- `src/test/java/`: Test cases for infrastructure utilities.

## Key Files
- `localstack-deploy.sh`: Local deployment script
- `pom.xml`: Maven configuration

## How to Use
- Run scripts in the root of this module for local/cloud setup.
- Build with Maven if Java code is present.
