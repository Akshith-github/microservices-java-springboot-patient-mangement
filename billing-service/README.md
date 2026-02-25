# Billing Service Documentation

## Overview
The Billing Service manages billing accounts and transactions, and communicates with other services via REST and gRPC. It is built using Spring Boot and supports both synchronous (HTTP) and asynchronous (gRPC/event) operations.

## Code Design & Processing Flow
- **REST APIs:**
	- Exposes endpoints for creating, updating, and retrieving billing accounts and transactions.
	- Validates incoming requests and interacts with the database for persistence.
- **gRPC/Event Processing:**
	- Defines Protobuf schemas in `src/main/proto/` for inter-service communication.
	- Consumes or produces events for billing-related actions (e.g., account creation, payment processed).
- **Business Logic:**
	- Service classes handle billing calculations, validation, and persistence.
- **Configuration:**
	- Service and gRPC settings are set in `application.properties`.

## Request/Event Handling Flow
1. **API Request:**
		- Client or another service sends a REST/gRPC request to the billing service.
2. **Validation & Processing:**
		- Service validates input, processes business logic, and updates the database.
3. **Event Emission (if applicable):**
		- Emits billing events to Kafka or gRPC consumers.
4. **Response:**
		- Returns result or status to the caller.

## Service Architecture Diagram

```mermaid
flowchart TB
    subgraph Clients
        Gateway["🌐 API Gateway<br/>REST (4001)"]
        Patient["👤 Patient Service<br/>gRPC (9001)"]
    end
    
    subgraph Billing_Service["💳 Billing Service"]
        REST_API["REST Controller<br/>Port 4001"]
        GRPC_Server["gRPC Server<br/>Port 9001"]
        Service["Billing Service Logic"]
        Repo["Repository Layer"]
    end
    
    subgraph Persistence
        DB[("🗄️ Billing DB<br/>PostgreSQL")]
        Kafka[["📨 Kafka<br/>BillingEvent"]]
    end
    
    Gateway -->|REST| REST_API
    Patient -->|gRPC| GRPC_Server
    REST_API --> Service
    GRPC_Server --> Service
    Service --> Repo
    Repo --> DB
    Service -->|Publish| Kafka
    
    style Billing_Service fill:#90EE90,stroke:#333
    style DB fill:#FFD700,stroke:#333
    style Kafka fill:#FFA07A,stroke:#333
```

```mermaid
sequenceDiagram
    participant Patient as 👤 Patient Service
    participant Billing as 💳 Billing Service
    participant DB as 🗄️ Database
    participant Kafka as 📨 Kafka
    
    Note over Patient,Kafka: gRPC Billing Account Creation
    Patient->>Billing: gRPC CreateBillingAccount(patientId)
    Billing->>DB: Insert billing record
    DB-->>Billing: Success
    Billing->>Kafka: Publish BillingEvent
    Billing-->>Patient: BillingAccount response
```

## Source Structure
- `src/main/java/`: Controllers, service classes, and gRPC/event logic.
- `src/main/resources/`: Configuration files (`application.properties`).
- `src/main/proto/`: Protobuf definitions for gRPC/event schema.
- `src/test/java/`: Test cases for billing logic.

## Key Files
- `Dockerfile`: Containerization setup
- `pom.xml`: Maven configuration

## How to Run
1. Build: `./mvnw clean install`
2. Run: `java -jar target/*.jar` or use Docker

## Notes
- Update Protobuf files in `src/main/proto/` for API/event schema changes.
