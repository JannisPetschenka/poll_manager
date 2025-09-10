FROM openjdk:21
WORKDIR /app
COPY . .

CMD ["/bin/bash", "-c", "./gradlew copyWebApp;./gradlew clean bootJar"]
COPY backend/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
