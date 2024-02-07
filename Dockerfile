# Use OpenJDK 17 as base image
FROM openjdk:17

# Set working directory
WORKDIR /app

# Copy the compiled JAR file into the container
COPY target/student_system-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
