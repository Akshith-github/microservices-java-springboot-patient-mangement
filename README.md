
# Microservices Patient Management Application

## Architecture Diagram
# diagram

```plantuml
@startuml
skinparam componentStyle rectangle
skinparam backgroundColor #FEFEFE

skinparam component {
  BackgroundColor<<gateway>> #6CA6CD
  BackgroundColor<<service>> #90EE90
  BackgroundColor<<database>> #FFD700
  BackgroundColor<<messaging>> #FFA07A
  BorderColor #333333
  FontColor #000000
}

skinparam package {
  BackgroundColor #E8E8E8
  BorderColor #666666
}

skinparam database {
  BackgroundColor #FFD700
  BorderColor #333333
}

skinparam queue {
  BackgroundColor #FFA07A
  BorderColor #333333
}

actor User #LightBlue

package "AWS Cloud" {
  package "PatientManagementVPC (2 AZs)" as VPC #E0F0FF {
    
    package "ECS Cluster: patient-management.local" as ECS #D0FFD0 {
      
      component "API Gateway\n(Port 4004)\n[ALB Enabled]" as API_Gateway <<gateway>>
      
      component "Auth Service\n(Port 4005)\nJWT_SECRET configured" as Auth_Service <<service>>
      
      component "Patient Service\n(Port 4000)\nBILLING_SERVICE_GRPC_PORT: 9001" as Patient_Service <<service>>
      
      component "Billing Service\n(Port 4001 REST)\n(Port 9001 gRPC)" as Billing_Service <<service>>
      
      component "Analytics Service\n(Port 4002)" as Analytics_Service <<service>>
    }
    
    package "RDS Databases (PostgreSQL 17.2)" as RDS #FFF8DC {
      database "auth-service-db\n(t2.micro, 20GB)" as AuthDB
      database "patient-service-db\n(t2.micro, 20GB)" as PatientDB
    }
    
    package "MSK Kafka Cluster" as MSK #FFE4E1 {
      queue "kafka-cluster\n(Kafka 3.6.0)\n2 Broker Nodes\nkafka.m5.xlarge" as Kafka
    }
  }
}

User --> API_Gateway : REST (4004)
API_Gateway --> Auth_Service : REST (4005)\nLogin/Validate
API_Gateway --> Patient_Service : REST (4000)\nCRUD
API_Gateway --> Billing_Service : REST (4001)\nBilling APIs
API_Gateway --> Analytics_Service : REST (4002)\nAnalytics

Patient_Service --> Billing_Service : gRPC (9001)
Patient_Service --> Kafka : Publish PatientEvent
Billing_Service --> Kafka : Publish BillingEvent
Analytics_Service <-- Kafka : Consume PatientEvent

Auth_Service --> AuthDB : JDBC (5432)
Patient_Service --> PatientDB : JDBC (5432)

note right of API_Gateway
  Spring Profile: prod
  AUTH_SERVICE_URL: host.docker.internal:4005
end note

note right of Patient_Service
  Depends on:
  - patient-service-db
  - billing-service
  - msk-cluster
end note

note right of Analytics_Service
  Depends on:
  - msk-cluster
end note

legend right
  |= Color |= Type |
  | <#6CA6CD> | API Gateway |
  | <#90EE90> | Microservices |
  | <#FFD700> | Databases |
  | <#FFA07A> | Messaging |
endlegend

@enduml
```

```plantuml
actor User
User -> API_Gateway: HTTP Request
API_Gateway -> Auth_Service: Auth/Token Validation
API_Gateway -> Patient_Service: Patient CRUD
Patient_Service -> Billing_Service: Billing APIs GRPC


Patient_Service -> Kafka: Publish Patient Events
Billing_Service -> Kafka: Publish Billing Events
Analytics_Service -> Kafka: Consume Patient Events

API_Gateway -[hidden]-> Kafka
Auth_Service -[hidden]-> Kafka

database DB1 as PatientDB
database DB2 as BillingDB
Patient_Service --> PatientDB
Auth_Service --> BillingDB
@enduml
```

### Mermaid Diagram (GitHub Native Support)

```mermaid
flowchart TB
    subgraph AWS_Cloud["☁️ AWS Cloud"]
        subgraph VPC["PatientManagementVPC (2 AZs)"]
            subgraph ECS["ECS Cluster: patient-management.local"]
                API_Gateway["🌐 API Gateway<br/>Port 4004<br/>ALB Enabled"]
                Auth_Service["🔐 Auth Service<br/>Port 4005<br/>JWT Auth"]
                Patient_Service["👤 Patient Service<br/>Port 4000"]
                Billing_Service["💳 Billing Service<br/>Port 4001 REST<br/>Port 9001 gRPC"]
                Analytics_Service["📊 Analytics Service<br/>Port 4002"]
            end
            
            subgraph RDS["RDS PostgreSQL 17.2"]
                AuthDB[("🗄️ auth-service-db<br/>t2.micro, 20GB")]
                PatientDB[("🗄️ patient-service-db<br/>t2.micro, 20GB")]
            end
            
            subgraph MSK["MSK Kafka"]
                Kafka[["📨 kafka-cluster<br/>Kafka 3.6.0<br/>2 Brokers"]]
            end
        end
    end
    
    User((👤 User)) -->|REST 4004| API_Gateway
    
    API_Gateway -->|REST 4005<br/>Login/Validate| Auth_Service
    API_Gateway -->|REST 4000<br/>CRUD| Patient_Service
    API_Gateway -->|REST 4001<br/>Billing APIs| Billing_Service
    API_Gateway -->|REST 4002<br/>Analytics| Analytics_Service
    
    Patient_Service -->|gRPC 9001| Billing_Service
    Patient_Service -->|Publish PatientEvent| Kafka
    Billing_Service -->|Publish BillingEvent| Kafka
    Kafka -->|Consume PatientEvent| Analytics_Service
    
    Auth_Service -->|JDBC 5432| AuthDB
    Patient_Service -->|JDBC 5432| PatientDB

    style API_Gateway fill:#6CA6CD,stroke:#333,color:#000
    style Auth_Service fill:#90EE90,stroke:#333,color:#000
    style Patient_Service fill:#90EE90,stroke:#333,color:#000
    style Billing_Service fill:#90EE90,stroke:#333,color:#000
    style Analytics_Service fill:#90EE90,stroke:#333,color:#000
    style AuthDB fill:#FFD700,stroke:#333,color:#000
    style PatientDB fill:#FFD700,stroke:#333,color:#000
    style Kafka fill:#FFA07A,stroke:#333,color:#000
```

## Overview
This project is a modular microservices-based system for managing patients, billing, analytics, authentication, and API gateway. Each service is independently deployable and communicates via REST, gRPC, or Kafka events.

## Architecture
- **api-gateway**: Routes and secures all client requests to backend services.
- **auth-services**: Handles authentication, token generation, and validation.
- **patient-service**: Manages CRUD operations for patient records and emits events for downstream processing.
- **billing-service**: Manages billing accounts and transactions, supporting both REST and gRPC/event-driven communication.
- **analytics-service**: Consumes patient events from Kafka and logs analytics data.
- **infrastructure**: Scripts and configuration for local/cloud deployment and environment setup.
- **integration-tests**: Contains integration and end-to-end tests for the entire system.

## Key Technologies
- Java (Spring Boot)
- Kafka (event-driven communication)
- gRPC (Protobuf-based inter-service communication)
- Docker (containerization)
- Maven (build and dependency management)

## Getting Started
1. Clone the repository.
2. Build all modules: `./mvnw clean install`
3. Start required infrastructure (e.g., Kafka, databases, LocalStack).
4. Run each service using Docker or `java -jar target/*.jar`.
5. Use the provided HTTP/gRPC request samples for testing.

## Module Documentation
Each module contains a README.md with detailed design, API/event processing, and flow descriptions.

## Contribution
- Follow Java and Maven best practices.
- Document new modules or changes in the respective README.md.

---

For more details, see the README.md inside each module folder.
der.
