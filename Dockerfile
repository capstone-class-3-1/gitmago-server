FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
COPY src/main/resources/keystore.p12 keystore.p12
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
