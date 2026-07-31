# REST Employee Service

Short Spring Boot REST API for managing employee records.

## Tech Stack
- Java
- Spring Boot
- Maven
- SQL schema/data initialization via `src/main/resources`

## Run
```bash
./mvnw spring-boot:run
```

On Windows:
```powershell
.\mvnw.cmd spring-boot:run
```

## Test
```powershell
.\mvnw.cmd test
```

## Project Structure
- `src/main/java/com/neueda/rest` - application source
- `src/test/java/com/neueda/rest` - tests
- `src/main/resources` - configuration, schema, and seed data

## Notes
This project includes controller, service, repository, and exception handling layers for a simple employee API.

