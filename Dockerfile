FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 443
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
