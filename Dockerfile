FROM openjdk:17-jdk
LABEL authors="soheeparklee"

COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]