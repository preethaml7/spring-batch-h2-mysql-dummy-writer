# Spring Batch Project with H2 and MySQL

This Spring Batch project demonstrates:

- A **dummy reader** that generates fake records.
- A **JPA ItemWriter** that writes the processed records to a **MySQL** database.
- **H2** used exclusively for internal Spring Batch **metadata tables**.

## Project Overview

- **H2 Database**: Used for storing metadata related to Spring Batch (e.g., job executions, step executions, etc.).
- **MySQL Database**: Stores the generated data records (fake records created by the dummy reader).

## Technologies Used:

- **Spring Boot** for application setup.
- **Spring Batch** for batch processing.
- **JPA (Java Persistence API)** for writing data to MySQL.
- **H2** for managing batch metadata tables.

## Setup and Configuration

### Databases:

- Configure the MySQL connection in `application.yml` (under `spring.datasource`).
- H2 will automatically be used by Spring Batch for metadata storage.

### Maven Dependencies:

- `spring-boot-starter-data-jpa` for JPA support.
- `spring-boot-starter-batch` for Spring Batch functionality.
- H2 and MySQL JDBC drivers for respective database support.

## How to Run:

1. Clone the repository.
2. Configure your MySQL database connection details in `application.yml`.
3. Run the Spring Boot application.

The application will:

- Generate fake records using the dummy reader.
- Write the records to MySQL.
- Use H2 for managing Spring Batch metadata.

## Useful Information:

- The H2 database is **only** used for internal Spring Batch metadata storage and will **not** store the processed
  records.
- Ensure that MySQL is properly configured before running the application.
- Spring Batch automatically handles batch job execution and metadata storage.

## Notes:

- This setup provides an embedded solution for Spring Batch where the metadata is isolated from the data being
  processed.
- If you encounter issues related to Spring Batch metadata or MySQL connection, ensure both H2 and MySQL are correctly
  configured.