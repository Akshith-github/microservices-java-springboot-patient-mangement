FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

ARG JWT_SECRET=dummy_secret_for_build_purposes_only
ENV JWT_SECRET=${JWT_SECRET}

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package

FROM openjdk:24-ea-21-jdk AS runner

WORKDIR /app

COPY --from=builder ./app/target/auth-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 4005

ENTRYPOINT ["java", "-jar", "app.jar"]