FROM gcr.io/distroless/java25-debian13:nonroot

ARG JAR_FILE=build/libs/*-all.jar

# Copy the fat JAR from the build output
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# Run as non-root user (distroless nonroot uid=65532)
USER 65532:65532

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]
