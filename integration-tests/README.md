# Integration Tests Documentation

## Overview
This module contains integration and end-to-end tests for the microservices application, ensuring that all services work together as expected.

## Code Design & Test Flow
- **Test Cases:**
	- Located in `src/test/java/`, covering cross-service scenarios and workflows.
	- Tests may use mock servers, embedded Kafka, or in-memory databases for isolation.
- **Execution:**
	- Tests are run using Maven's test lifecycle.
	- Results are reported in the console and as test reports.

## Test Execution Flow
1. **Setup:**
		- Test environment is initialized (databases, Kafka, etc.).
2. **Test Execution:**
		- Integration tests are run, invoking APIs and verifying inter-service behavior.
3. **Teardown:**
		- Resources are cleaned up after tests.

## Test Flow Diagram

```mermaid
flowchart TB
    subgraph Test_Execution["🧪 Integration Test Execution"]
        Setup["1️⃣ Setup<br/>Init DBs, Kafka, Mocks"]
        Tests["2️⃣ Execute Tests<br/>API calls, assertions"]
        Teardown["3️⃣ Teardown<br/>Cleanup resources"]
    end
    
    subgraph Test_Environment["🔧 Test Environment"]
        MockDB[("🗄️ In-Memory DB")]
        MockKafka[["📨 Embedded Kafka"]]
        Services["🧩 Service Mocks"]
    end
    
    Setup --> MockDB
    Setup --> MockKafka
    Setup --> Services
    Tests -->|Invoke APIs| Services
    Tests -->|Verify Events| MockKafka
    Tests -->|Check Data| MockDB
    Teardown --> MockDB
    Teardown --> MockKafka
    
    style Test_Execution fill:#E8E8E8,stroke:#333
    style Test_Environment fill:#D0FFD0,stroke:#333
```

```mermaid
sequenceDiagram
    participant Maven as 🔨 Maven
    participant Test as 🧪 Test Class
    participant API as 🌐 Service APIs
    participant DB as 🗄️ Test DB
    participant Kafka as 📨 Kafka
    
    Maven->>Test: Run tests
    Test->>Test: @BeforeAll Setup
    Test->>DB: Initialize schema
    Test->>Kafka: Create topics
    
    loop Each Test Case
        Test->>API: HTTP Request
        API->>DB: CRUD operations
        API->>Kafka: Publish events
        API-->>Test: Response
        Test->>Test: Assert results
    end
    
    Test->>Test: @AfterAll Teardown
    Test->>DB: Drop schema
    Test->>Kafka: Delete topics
```

## Source Structure
- `src/test/java/`: Integration and end-to-end test cases.

## Key Files
- `pom.xml`: Maven configuration

## How to Run
Use Maven to run the integration tests:

```sh
./mvnw test
```
