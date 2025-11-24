FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY lotto-bot-client/build/libs/*.jar app.jar
EXPOSE 8200
CMD ["java", "-jar", "app.jar"]