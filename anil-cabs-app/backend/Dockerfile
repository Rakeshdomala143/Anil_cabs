FROM gradle:8.7-jdk20 AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew && ./gradlew --no-daemon bootJar
FROM eclipse-temurin:20-jre
WORKDIR /app
COPY --from=build /app/build/libs/anil-cabs-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
