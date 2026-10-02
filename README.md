# TRISHA ACADEMY — DevOps Java Demo

Minimal Spring Boot 3 app with a single GET API.

## Requirements

- JDK 17+
- Maven 3.8+

## Run

```bash
mvn spring-boot:run
```

Open http://localhost:8080/name in the browser — it shows **your name **.

## Build

```bash
mvn clean package
java -jar target/devops-java-demo-1.0.0.jar
```

PS C:\Users\admin> netstat -ano | findstr :8080
  TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       15348
  TCP    [::]:8080              [::]:0                 LISTENING       15348
PS C:\Users\admin> taskkill /PID 15348 /F
SUCCESS: The process with PID 15348 has been terminated.
PS C:\Users\admin>
