**Spring Boot CRUD Application with ActiveMQ, SonarQube, Redis, Swagger, and Spring Security
**

Overview:
This Spring Boot application demonstrates CRUD (Create, Read, Update, Delete) operations. It integrates ActiveMQ for messaging, SonarQube for code quality analysis, Redis for caching, Swagger for API documentation, and Spring Security for token-based authentication.
Prerequisites
Before you begin, make sure you have the following installed:
•	Java JDK (version 8 or later)
•	Maven
•	ActiveMQ
•	SonarQube
•	Redis
Getting Started
1.	Clone the repository:
      bashCopy code
      git clone https://github.com/Swathisuva/MyWebApp.git cd spring-boot-crud
2.	Build the project:
      bashCopy code
      mvn clean install
3.	Run the application:
      bashCopy code
      java -jar target/student_system-0.0.1-SNAPSHOT.jar
4.	Access the Swagger API documentation at http://localhost:8080/swagger-ui.html
      Authentication
      •	Spring Security is enabled with token-based authentication.
      •	Obtain a token by calling the /authenticate endpoint with valid credentials.
      •	Include the token in the Authorization header for subsequent API requests.
      •
      Usage
      •	Read: The list of student is displayed on the home page.
      •	Update: Click on the "Edit" link next to an entity to update its information.
      •	Delete: Click on the "Delete" link next to an entity to remove it from the database.
      Configuration
      •	Database configuration can be found in src/main/resources/application.properties.
      •	ActiveMQ configuration is in src/main/resources/application.properties.
      •	Redis configuration is in src/main/resources/application.properties.
      Customize the application properties, including ActiveMQ, Redis, and other settings based on your environment.

      Technologies Used
      •	Spring Boot
      •	Spring Data JPA
      •	Swagger
      •	Spring Security
      •	ActiveMQ
      •	SonarQube
      •	Redis
     
      Acknowledgments
      •	Special thanks to Spring Framework for making Java development enjoyable.

