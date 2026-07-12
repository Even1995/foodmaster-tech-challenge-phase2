FROM openjdk:21-ea-jdk-slim

WORKDIR /app

COPY target/foodmaster-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
