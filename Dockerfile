# Multi-stage build for auth-service
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY pom.xml .

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Final runtime stage
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "/app/app.jar"]