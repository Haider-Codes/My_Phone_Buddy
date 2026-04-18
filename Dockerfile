FROM maven:3.9.14-eclipse-temurin-21 AS build
LABEL authors="SYED HAIDER RAZA"

# Stage 1: Build the application
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]