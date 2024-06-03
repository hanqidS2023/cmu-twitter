# Use a base image with Java and Maven
FROM maven:3.8.4-openjdk-17-slim as build
# Copy the project files to the container
COPY src /app/src
COPY pom.xml /app
# Set the working directory in the container
WORKDIR /app
# Build the application and skip tests to speed up the build
RUN mvn clean package -DskipTests
# Use a slim version of the JDK for the final image
FROM openjdk:17-jdk-slim
# Set the working directory in the container
WORKDIR /app
# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar /app/app.jar
# Expose the port the app runs on
EXPOSE 9000
# Command to run the application
CMD ["java", "-jar", "/app/app.jar"]
