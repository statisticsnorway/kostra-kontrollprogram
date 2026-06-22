FROM bellsoft/liberica-openjdk-alpine-musl:21
RUN addgroup -S nonroot && adduser -S nonroot -G nonroot
ARG JAR_FILE=build/libs/*-all.jar
COPY ${JAR_FILE} app.jar
USER nonroot:nonroot
ENTRYPOINT ["java","-jar","/app.jar"]