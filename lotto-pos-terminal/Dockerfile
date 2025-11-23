FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts ./
COPY lotto-pos-terminal/build.gradle.kts lotto-pos-terminal/
RUN chmod +x ./gradlew

RUN ./gradlew :lotto-pos-terminal:dependencies --no-daemon

COPY lotto-pos-terminal/src/ lotto-pos-terminal/src/
RUN ./gradlew :lotto-pos-terminal:build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/lotto-pos-terminal/build/libs/*.jar app.jar
EXPOSE 8400
CMD ["java", "-jar", "app.jar"]