# Stage 1: Build the JAR
FROM openjdk:27-ea-trixie AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:27-ea-trixie
WORKDIR /app
COPY --from=build /app/target/Dibs.jar Dibs.jar
ENTRYPOINT ["java","-jar","Dibs.jar"]
