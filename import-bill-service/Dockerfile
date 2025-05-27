# Multi-stage build for import-bill-service
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy parent pom first for better caching
COPY pom.xml ./pom.xml

# Copy all service directories (needed for parent pom resolution)
COPY user-service ./user-service
COPY costume-service ./costume-service
COPY bill-costume-service ./bill-costume-service
COPY supplier-service ./supplier-service
COPY import-bill-service ./import-bill-service

# Build the application
RUN mvn clean package -DskipTests -pl import-bill-service

# Runtime stage
FROM eclipse-temurin:17-jre

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create app user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Set working directory
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/import-bill-service/target/*.jar app.jar

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8085

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8085/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
