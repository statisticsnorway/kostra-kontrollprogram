# Use Distroless Java 25 runtime, pinned by digest (includes libbrotli1 1.1.0-2+deb13u1, CVE-2026-111497)
FROM gcr.io/distroless/java25-debian13:nonroot@sha256:5d3177252775d4b01603470fe21c015393019089904f68724bd37242e4bc6e6d

# Set working directory
WORKDIR /app

# Copy the fat JAR from the build output
COPY build/libs/*-all.jar app.jar

# Expose application port
EXPOSE 8080

# Run as non-root user (distroless nonroot uid=65532)
USER 65532:65532

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
