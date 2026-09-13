# Multi-stage Dockerfile for Spring Boot App
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build JAR file without running tests (fast build)
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy compiled JAR from build stage
COPY --from=build /app/target/portfolio-1.0.0.jar app.jar

# Expose default port
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
