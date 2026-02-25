# Auth Services Documentation

## Overview
The Auth Service manages authentication and authorization for the application, including token generation and validation. It is built using Spring Boot and Spring Security.

## Code Design & Authentication Flow
- **Authentication:**
	- Exposes REST endpoints for login and token validation (e.g., `/auth/login`, `/auth/validate`).
	- Validates user credentials against a user store (database or in-memory).
	- Generates JWT or similar tokens upon successful authentication.
- **Authorization:**
	- Validates tokens on protected endpoints.
	- Integrates with the API Gateway and other services for secure access.
- **Configuration:**
	- Security settings and secrets are set in `application.properties`.

## Request Handling Flow
1. **Login Request:**
		- Client sends credentials to `/auth/login`.
2. **Credential Validation:**
		- Service checks credentials and, if valid, generates a token.
3. **Token Usage:**
		- Client uses token for subsequent requests to protected endpoints.
4. **Token Validation:**
		- `/auth/validate` endpoint checks token validity for other services.

## Authentication Flow Diagram

```mermaid
sequenceDiagram
    participant User as 👤 User
    participant Gateway as 🌐 API Gateway
    participant Auth as 🔐 Auth Service
    participant DB as 🗄️ Auth DB
    
    Note over User,DB: Login Flow
    User->>Gateway: POST /auth/login (credentials)
    Gateway->>Auth: Forward login request
    Auth->>DB: Validate credentials
    DB-->>Auth: User data
    Auth-->>Auth: Generate JWT Token
    Auth-->>Gateway: JWT Token
    Gateway-->>User: JWT Token
    
    Note over User,DB: Token Validation Flow
    User->>Gateway: Request with JWT Header
    Gateway->>Auth: POST /auth/validate (token)
    Auth-->>Auth: Verify JWT signature
    Auth-->>Gateway: Token valid/invalid
    Gateway-->>User: Allow/Deny access
```

```mermaid
flowchart TB
    subgraph Auth_Service["🔐 Auth Service (Port 4005)"]
        Login["/auth/login"]
        Validate["/auth/validate"]
        JWT["JWT Generator"]
        Security["Spring Security"]
    end
    
    subgraph Database["🗄️ PostgreSQL"]
        AuthDB[("auth-service-db")]
    end
    
    Login --> Security
    Security --> AuthDB
    Security --> JWT
    Validate --> JWT
    
    style Auth_Service fill:#90EE90,stroke:#333
    style AuthDB fill:#FFD700,stroke:#333
```

## Source Structure
- `src/main/java/`: Controllers, authentication logic, and security config.
- `src/main/resources/`: Service configuration (`application.properties`).
- `src/test/java/`: Test cases for authentication flows.

## Key Files
- `Dockerfile`: Containerization setup
- `pom.xml`: Maven configuration

## How to Run
1. Build: `./mvnw clean install`
2. Run: `java -jar target/*.jar` or use Docker

## Notes
- Configure authentication providers and secrets in `application.properties`.
