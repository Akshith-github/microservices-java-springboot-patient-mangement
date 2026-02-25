# Analytics Service Documentation

## Overview
The Analytics Service is designed to consume patient-related events from a Kafka topic, process them, and log relevant analytics information. It is built using Spring Boot and leverages Kafka for event-driven communication.

## Code Design & Event Flow
- **Event-Driven Architecture:**
	- The service listens to the `patient` Kafka topic for incoming events.
	- Events are serialized using Protocol Buffers (see `src/main/proto/patient_event.proto`).
- **Main Components:**
	- `KafkaConsumer` (in `src/main/java/com/pm/analyticsservice/kafka/`):
		- Annotated with `@Service` and `@KafkaListener`.
		- Listens for messages on the `patient` topic.
		- Deserializes incoming byte arrays into `PatientEvent` objects using Protobuf.
		- Logs patient event details (ID, name, email) for analytics purposes.
	- `AnalyticsServiceApplication`:
		- Standard Spring Boot application entry point.
- **Configuration:**
	- Kafka consumer properties are set in `src/main/resources/application.properties`.
	- Uses `ByteArrayDeserializer` for value deserialization to handle Protobuf messages.

## Event Processing Flow
1. **Kafka Event Reception:**
		- The `KafkaConsumer` receives a message from the `patient` topic.
2. **Deserialization:**
		- The message (byte array) is parsed into a `PatientEvent` object using the generated Protobuf class.
3. **Logging/Analytics:**
		- Patient details (ID, name, email) are extracted and logged for analytics.
4. **Error Handling:**
		- If deserialization fails, an error is logged.

## API & Endpoints
- This service does not expose REST APIs by default. It operates as a background event processor.
- All analytics are handled internally via Kafka event consumption and logging.

## Source Structure
- `src/main/java/`: Contains the main application and Kafka consumer logic.
- `src/main/resources/`: Configuration files (`application.properties`).
- `src/main/proto/`: Protobuf definitions for event schema.
- `src/test/java/`: Test cases for analytics logic (if present).

## Key Files
- `Dockerfile`: Containerization setup
- `pom.xml`: Maven configuration

## How to Run
1. Build: `./mvnw clean install`
2. Run: `java -jar target/*.jar` or use Docker

## Notes
- Update Protobuf files in `src/main/proto/` for event schema changes.
- Ensure Kafka and the `patient` topic are running before starting this service.
