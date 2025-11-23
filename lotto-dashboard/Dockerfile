FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts ./
COPY lotto-dashboard/build.gradle.kts lotto-dashboard/
RUN chmod +x ./gradlew

RUN ./gradlew :lotto-dashboard:dependencies --no-daemon

COPY lotto-dashboard/src/ lotto-dashboard/src/
RUN ./gradlew :lotto-dashboard:build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/lotto-dashboard/build/libs/*.jar app.jar
EXPOSE 8500
CMD ["java", "-jar", "app.jar"]