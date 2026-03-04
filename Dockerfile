## Use official Java 21 image
#FROM eclipse-temurin:21-jdk-alpine
#
## Set working directory
#WORKDIR /app
#
## Copy jar file
#COPY target/*.jar app.jar
#
## Run application
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1 - Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2 - Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]