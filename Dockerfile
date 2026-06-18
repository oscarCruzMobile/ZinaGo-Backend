# Stage 1: Build con JDK 21
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
# Asegura permisos de ejecución para el wrapper de Gradle y compila
RUN chmod +x ./gradlew && ./gradlew bootJar -x test

# Stage 2: Runtime con JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]