# syntax=docker/dockerfile:1.7

FROM gradle:8.10-jdk21-alpine AS build
WORKDIR /workspace

COPY --chown=gradle:gradle settings.gradle build.gradle ./
COPY --chown=gradle:gradle gradle ./gradle
RUN gradle --no-daemon dependencies || true

COPY --chown=gradle:gradle src ./src
RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

RUN groupadd --system spring \
 && useradd --system --gid spring --home /app spring \
 && chown -R spring:spring /app
USER spring

COPY --from=build --chown=spring:spring /workspace/build/libs/*.jar /app/app.jar

ENV JAVA_OPTS=""
ENV SERVER_PORT=8080
EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=40s --retries=10 \
  CMD bash -c '</dev/tcp/127.0.0.1/${SERVER_PORT:-8080}' || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
