# Stage 1: Build stage
FROM eclipse-temurin:26-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper and POM for dependency caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B || true

# Copy source code and build production package
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Lightweight Runtime stage
FROM eclipse-temurin:26-jre-alpine
WORKDIR /app

# Copy compiled JAR artifact from builder stage
COPY --from=builder /app/target/factos-0.0.1-SNAPSHOT.jar app.jar

# Expose default HTTP port
EXPOSE 8080
ENV PORT=8080

# Execute Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
