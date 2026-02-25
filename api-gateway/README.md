# API Gateway Documentation

## Overview
The API Gateway acts as the single entry point for all client requests, routing them to the appropriate microservice. It is built using Spring Boot and typically uses Spring Cloud Gateway or Zuul for routing and filtering.

## Code Design & Request Flow
- **Routing:**
	- Defines routes to backend microservices (patient, billing, analytics, auth) in configuration or Java code.
	- Applies filters for authentication, logging, and request transformation.
- **API Processing:**
	- Receives HTTP requests from clients.
	- Validates and authenticates requests (often via JWT or OAuth2).
	- Forwards requests to the correct microservice based on path or headers.
	- Aggregates responses if needed and returns to the client.
- **Configuration:**
	- Routing and filter rules are set in `application.properties` or Java config classes.

## Request Handling Flow
1. **Client Request:**
		- Client sends HTTP request to the gateway.
2. **Pre-processing:**
		- Authentication and logging filters are applied.
3. **Routing:**
		- Request is routed to the appropriate microservice.
4. **Response Aggregation:**
		- (Optional) Gateway aggregates responses from multiple services.
5. **Response to Client:**
		- Gateway returns the final response to the client.

## Architecture Diagram

```mermaid
flowchart LR
    subgraph Client
        User((👤 User))
    end
    
    subgraph API_Gateway["🌐 API Gateway (Port 4004)"]
        Filter["🔒 Auth Filter"]
        Router["🔀 Router"]
        Aggregator["📦 Response Aggregator"]
    end
    
    subgraph Backend_Services["Backend Microservices"]
        Auth["🔐 Auth Service<br/>Port 4005"]
        Patient["👤 Patient Service<br/>Port 4000"]
        Billing["💳 Billing Service<br/>Port 4001"]
        Analytics["📊 Analytics Service<br/>Port 4002"]
    end
    
    User -->|HTTP Request| Filter
    Filter -->|Validate JWT| Auth
    Filter --> Router
    Router -->|/patients/**| Patient
    Router -->|/billing/**| Billing
    Router -->|/analytics/**| Analytics
    Patient --> Aggregator
    Billing --> Aggregator
    Analytics --> Aggregator
    Aggregator -->|HTTP Response| User
    
    style API_Gateway fill:#6CA6CD,stroke:#333
    style Auth fill:#90EE90,stroke:#333
    style Patient fill:#90EE90,stroke:#333
    style Billing fill:#90EE90,stroke:#333
    style Analytics fill:#90EE90,stroke:#333
```

## Source Structure
- `src/main/java/`: Gateway logic, routing, and filters.
- `src/main/resources/`: Configuration files (`application.properties`).
- `src/test/java/`: Unit and integration tests.

## Key Files
- `Dockerfile`: Containerization setup
- `pom.xml`: Maven configuration

## How to Run
1. Build: `./mvnw clean install`
2. Run: `java -jar target/*.jar` or use Docker

## Notes
- Update `application.properties` for custom routes or security settings.
