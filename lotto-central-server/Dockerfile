FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copy Gradle files first (rarely change)
COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts ./
COPY lotto-central-server/build.gradle.kts lotto-central-server/
RUN chmod +x ./gradlew

RUN ./gradlew :lotto-central-server:dependencies --no-daemon

COPY lotto-central-server/src/ lotto-central-server/src/

RUN ./gradlew :lotto-central-server:build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/lotto-central-server/build/libs/*.jar app.jar
EXPOSE 8100
CMD ["java", "-jar", "app.jar"]