FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts ./
COPY lotto-bot-client/build.gradle.kts lotto-bot-client/
RUN chmod +x ./gradlew

RUN ./gradlew :lotto-bot-client:dependencies --no-daemon

COPY lotto-bot-client/src/ lotto-bot-client/src/
RUN ./gradlew :lotto-bot-client:build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/lotto-bot-client/build/libs/*.jar app.jar
EXPOSE 8200
CMD ["java", "-jar", "app.jar"]