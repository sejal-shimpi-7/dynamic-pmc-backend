# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# --- THIS IS THE NEW LINE THAT FIXES THE ERROR ---
# Grant execute permission to the mvnw script
RUN chmod +x ./mvnw

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Copy the project source code
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Expose the port the app runs on
EXPOSE 3344

# Define the command to run your app
ENTRYPOINT ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]