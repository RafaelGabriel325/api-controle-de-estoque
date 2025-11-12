# Multi-stage build for smaller, production-ready image
# 1) Build stage
FROM maven:3.8.7-eclipse-temurin-19 AS builder
WORKDIR /workspace

# Copy only pom first to leverage Docker layer caching for dependencies
COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

# Now copy the source and build
COPY src ./src
RUN mvn -B -q -DskipTests clean package

# 2) Runtime stage (use slim JRE only)
FROM eclipse-temurin:19-jre
WORKDIR /app

# Copy the built artifact from the builder stage
COPY --from=builder /workspace/target/controleestoque-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose default app port (env at build-time doesn't work for EXPOSE)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
