FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY lotto-central-server/build/libs/*.jar app.jar
EXPOSE 8100
CMD ["java", "-jar", "app.jar"]