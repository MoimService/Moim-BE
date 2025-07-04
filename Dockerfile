FROM eclipse-temurin:17-jdk-alpine
#FROM openjdk:17-jdk
LABEL authors="soheeparklee"

#COPY build/libs/*SNAPSHOT.jar /app.jar
COPY ./build/libs/*SNAPSHOT.jar proejct.jar

#ENTRYPOINT ["java", "-jar", "/app.jar"]
ENTRYPOINT ["java", "-jar", "project.jar"]
