# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Use -Xmx384m to stay safely within 512MB RAM, and UseContainerSupport for better docker awareness
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xmx384m", "-Xms256m", "-jar", "app.jar"]
