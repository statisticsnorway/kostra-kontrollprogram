# Use Distroless Java 25 runtime, pinned by digest (includes libbrotli1 1.1.0-2+deb13u1, CVE-2026-111497)
FROM gcr.io/distroless/java25-debian13:nonroot@sha256:8ac5525549d14b8ddd34562f9fc8e6ba4840c995363a1507b17cadf8bb7ab57e

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
