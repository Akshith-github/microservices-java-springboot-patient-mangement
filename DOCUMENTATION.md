# Microservices Application Documentation

## Overview
This project is a microservices-based application for patient management, billing, analytics, authentication, and API gateway. Each service is implemented as a separate module, allowing for independent development, deployment, and scaling.

## Modules

### 1. api-gateway
- **Purpose:** Acts as the entry point for all client requests, routing them to the appropriate microservice.
- **Key Files:**
  - `Dockerfile`: Containerization setup
  - `pom.xml`: Maven configuration
  - `src/main/java/`: Gateway logic

### 2. auth-services
- **Purpose:** Handles authentication and authorization for the application.
- **Key Files:**
  - `Dockerfile`: Containerization setup
  - `pom.xml`: Maven configuration
  - `src/main/java/`: Auth logic
  - `src/main/resources/application.properties`: Auth service configuration

### 3. billing-service
- **Purpose:** Manages billing accounts and transactions.
- **Key Files:**
  - `Dockerfile`: Containerization setup
  - `pom.xml`: Maven configuration
  - `src/main/java/`: Billing logic
  - `src/main/resources/`: Billing configuration
  - `src/main/proto/`: Protobuf definitions

### 4. patient-service
- **Purpose:** Manages patient records and related operations.
- **Key Files:**
  - `Dockerfile`: Containerization setup
  - `pom.xml`: Maven configuration
  - `src/main/java/`: Patient logic
  - `src/main/resources/`: Patient configuration
  - `src/main/proto/`: Protobuf definitions

### 5. analytics-service
- **Purpose:** Provides analytics and reporting features.
- **Key Files:**
  - `Dockerfile`: Containerization setup
  - `pom.xml`: Maven configuration
  - `src/main/java/`: Analytics logic
  - `src/main/resources/`: Analytics configuration
  - `src/main/proto/`: Protobuf definitions

### 6. infrastructure
- **Purpose:** Contains infrastructure-related code and scripts, such as deployment automation.
- **Key Files:**
  - `localstack-deploy.sh`: Local deployment script
  - `pom.xml`: Maven configuration

### 7. integration-tests
- **Purpose:** Contains integration tests for the entire system.
- **Key Files:**
  - `pom.xml`: Maven configuration
  - `src/test/java/`: Test cases

### 8. api-requests & grpc-requests
- **Purpose:** Contains HTTP and gRPC request samples for testing and development.

## Common Features
- **Dockerized:** Each service/module has a Dockerfile for containerization.
- **Maven:** All modules use Maven for build and dependency management.
- **Protobuf:** Used for gRPC communication between services.

## Getting Started
1. Clone the repository.
2. Build each module using Maven (`./mvnw clean install`).
3. Use Docker to build and run each service.
4. Use the provided HTTP/gRPC request samples for testing.

## Contribution
- Follow standard Java and Maven best practices.
- Document any new modules or changes in this file.

---

# Module-Level Documentation

## api-gateway
- Handles routing and API aggregation.
- Configurable via `application.properties`.

## auth-services
- Manages user authentication and token validation.
- Integrates with other services for secure access.

## billing-service
- Handles billing account creation and transaction management.
- Uses gRPC for inter-service communication.

## patient-service
- Manages CRUD operations for patient data.
- Supports event-driven updates and notifications.

## analytics-service
- Aggregates and analyzes data from other services.
- Provides reporting endpoints.

## infrastructure
- Scripts and configuration for local and cloud deployment.

## integration-tests
- End-to-end and integration test cases for all modules.

## api-requests & grpc-requests
- Sample requests for manual and automated testing.

---

For detailed module-specific documentation, see the README or documentation file inside each module's directory if available.
