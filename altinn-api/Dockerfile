FROM bellsoft/liberica-runtime-container:jre-25-slim-musl

RUN addgroup -S nonroot && adduser -S nonroot -G nonroot && \
    apk update && \
    apk upgrade --no-cache libcrypto3 libssl3

ARG JAR_FILE=build/libs/*-all.jar

COPY ${JAR_FILE} app.jar

USER nonroot:nonroot

ENTRYPOINT ["java","-jar","/app.jar"]