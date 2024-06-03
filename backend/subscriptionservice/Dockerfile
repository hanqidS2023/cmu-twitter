# Use a slim version of the OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Install necessary utilities and clean up in one layer to reduce image size
RUN apt-get update && \
    apt-get install -y maven curl && \
    rm -rf /var/lib/apt/lists/*

# Copy only the pom.xml to leverage Docker cache
COPY pom.xml /app/

# Install dependencies in a separate layer to improve build time
RUN mvn dependency:go-offline

# Copy the rest of the project
COPY . /app/

# Build the application without running tests
RUN mvn clean install -DskipTests

# Expose the port the app runs on
EXPOSE 8082

# Command to run the application
CMD ["mvn", "spring-boot:run"]
