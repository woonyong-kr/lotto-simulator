FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY lotto-pos-terminal/build/libs/*.jar app.jar
EXPOSE 8400
CMD ["java", "-jar", "app.jar"]