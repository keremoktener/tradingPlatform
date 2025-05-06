# ────────────────────────────────
# 1️⃣  Build stage
# ────────────────────────────────
FROM gradle:8.11.1-jdk21-alpine AS build
WORKDIR /home/gradle/src

# Copy source *after* wrapper files to maximise the cache hit‑rate
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
COPY src src

# Re‑use Gradle cache between builds (requires BuildKit: DOCKER_BUILDKIT=1)
RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew clean bootJar

# ────────────────────────────────
# 2️⃣  Runtime stage
# ────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
LABEL org.opencontainers.image.source="https://github.com/your‑org/tradingPlatform"

# Spring Boot needs a writable /tmp for Tomcat; good to keep it explicit
VOLUME /tmp

# Copy the fat jar produced by Spring Boot
ARG JAR_PATH=/home/gradle/src/build/libs
COPY --from=build ${JAR_PATH}/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]