#FROM eclipse-temurin:23-jdk-alpine AS builder
#LABEL authors="jones"
#
#WORKDIR /build
#
#COPY mvnw pom.xml ./
#
#COPY .mvn .mvn
#
#COPY src src
#
#RUN chmod +x mvnw && ./mvnw clean package -DskipTests
#
## Stage 2: Create a lightweight runtime image
#FROM eclipse-temurin:23-jre-alpine
#WORKDIR /app
#
## Copy the built JAR from the builder stage
#COPY --from=builder /build/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
#
#
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]