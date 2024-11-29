# Job Search Application

The Job Search Application is a Spring Boot-based Java 11 application that enables clients to register and create or search for job postings provided by them. The application uses an in-memory H2 database.

### Requirements

- Java 11
- Maven

### Configuration

After cloning or downloading the source code of the application, run the command `mvn clean install` to download the necessary dependencies.
To start the application, use the command `mvn spring-boot:run`.

The application includes Swagger UI documentation, available at: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

The database can be accessed at the following URL (Driver class: `org.h2.Driver`, JDBC URL: `jdbc:h2:mem:testdb`, user: `sa`, pass: `password`):
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### Potential Improvements

- Develop a user interface.
- Enable modification and deletion of client data.
- Allow modification and deletion of job postings.
- Add additional fields to job postings (e.g., description, requirements, benefits).
- Implement user roles with passwords and permissions.
- Encrypt sensitive data stored in the database.
- Ensure scalability for increased workloads.
- Improve error handling and introduce logging.
- Enable regular database backups.
- Create automated tests.
