FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts ./
COPY lotto-pos-manager/build.gradle.kts lotto-pos-manager/
RUN chmod +x ./gradlew

RUN ./gradlew :lotto-pos-manager:dependencies --no-daemon

COPY lotto-pos-manager/src/ lotto-pos-manager/src/
RUN ./gradlew :lotto-pos-manager:build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/lotto-pos-manager/build/libs/*.jar app.jar
EXPOSE 8300
CMD ["java", "-jar", "app.jar"]