FROM eclipse-temurin:23-jdk-alpine AS builder
LABEL authors="jones"

WORKDIR /build

COPY mvnw pom.xml ./

COPY .mvn .mvn

COPY src src

RUN chmod +x mvnw && ./mvnw clean package -DskipTests


FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

COPY --from=builder /build/target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar


ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]