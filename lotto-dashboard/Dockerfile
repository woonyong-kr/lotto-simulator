FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY lotto-dashboard/build/libs/*.jar app.jar
EXPOSE 8500
CMD ["java", "-jar", "app.jar"]