# Stage 1: Build the JAR
FROM eclipse-temurin:25 AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/Dibs.jar Dibs.jar
ENTRYPOINT ["java","-jar","Dibs.jar"]
