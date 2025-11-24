FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY lotto-pos-manager/build/libs/*.jar app.jar
EXPOSE 8300
CMD ["java", "-jar", "app.jar"]