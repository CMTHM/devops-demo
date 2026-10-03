# TRISHA ACADEMY — DevOps Java Demo

Minimal Spring Boot 3 app with a Welcome API and a Student CRUD API backed by an H2 database, documented with Swagger (OpenAPI).

## Requirements

- JDK 17+
- Maven 3.8+

## Run

```bash
mvn spring-boot:run
```

The app starts on port **8080**. Open http://localhost:8080/name in the browser — it shows **your name**.

## APIs

| API | Operation | Method | URL |
|-----|-----------|--------|-----|
| Welcome | Show name | GET | `/name` |
| Students | Insert | POST | `/students` |
| Students | Get all | GET | `/students` |
| Students | Get one | GET | `/students/{id}` |
| Students | Update | PUT | `/students/{id}` |
| Students | Delete | DELETE | `/students/{id}` |

Student data is stored in the H2 database (see below).

## Database (H2)

The app uses an **H2 in-memory database** through Spring Data JPA.

| Setting | Value |
|---------|-------|
| JDBC URL | `jdbc:h2:mem:trishadb` |
| Username | `sa` |
| Password | `sa@123` |
| Table | `students` (created automatically from the `Student` entity) |

- `model/Student.java` is the JPA entity mapped to the `students` table.
- `repository/StudentRepository.java` extends `JpaRepository`, which provides save, find, and delete.
- The database lives inside the running app, so data is lost when the app stops or restarts.
- SQL statements run by Hibernate are printed in the console (`spring.jpa.show-sql=true`).

### View the data in the H2 console

1. Start the app and open http://localhost:8080/h2-console
2. Set **JDBC URL** to `jdbc:h2:mem:trishadb`, **User Name** to `sa`, and **Password** to `sa@123`.
3. Click **Connect**, then run:

   ```sql
   SELECT * FROM students;
   ```

## Swagger (OpenAPI)

Swagger gives a web page that lists every API and lets you call it from the browser, without Postman or curl.

### How Swagger is integrated

1. **Dependency** — `pom.xml` includes springdoc-openapi, which scans the controllers and generates the docs automatically:

   ```xml
   <dependency>
     <groupId>org.springdoc</groupId>
     <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
     <version>2.5.0</version>
   </dependency>
   ```

   springdoc 2.x is for Spring Boot 3. Spring Boot 2 projects need springdoc 1.x instead.

2. **API info** — `config/OpenApiConfig.java` sets the title, description and version shown at the top of the Swagger page:

   ```java
   @Bean
   public OpenAPI devopsJavaDemoOpenApi() {
       return new OpenAPI()
               .info(new Info()
                       .title("TRISHA ACADEMY DevOps Java Demo API")
                       .description("Welcome API and Student CRUD API")
                       .version("1.0.0"));
   }
   ```

3. **Annotations** — the controllers use `@Tag` to group APIs and `@Operation` to describe each endpoint:

   ```java
   @RestController
   @RequestMapping("/students")
   @Tag(name = "Students", description = "Student CRUD API")
   public class StudentController {

       @PostMapping
       @Operation(summary = "Insert a student")
       public ResponseEntity<Student> insert(@RequestBody Student student) { ... }
   }
   ```

   These annotations are optional; Swagger still lists every endpoint without them.

No extra configuration is needed in `application.properties`.

### Swagger URLs

| Page | URL |
|------|-----|
| Swagger UI (interactive page) | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON (raw API spec) | http://localhost:8080/v3/api-docs |

### Step-by-step: access all APIs from Swagger

1. Start the app with `mvn spring-boot:run` and wait for `Started SpringLabApplication` in the console.
2. Open http://localhost:8080/swagger-ui/index.html in the browser.
3. You will see two groups: **Welcome** and **Students**. Click an endpoint to expand it, then click **Try it out** to call it.

**Show name — `GET /name`**

1. Expand **GET /name** under **Welcome**.
2. Click **Try it out**, then **Execute**.
3. Response code **200** with body `My name is Bhagyashree Behera`.

**Insert a student — `POST /students`**

1. Expand **POST /students** and click **Try it out**.
2. Replace the request body with:

   ```json
   {
     "name": "Priya",
     "course": "DevOps"
   }
   ```

   Leave out `id`; the server assigns it.
3. Click **Execute**. Response code **201** with the saved student, e.g. `{"id": 1, "name": "Priya", "course": "DevOps"}`.

**Get all students — `GET /students`**

1. Expand **GET /students**, click **Try it out**, then **Execute**.
2. Response code **200** with a list of all students.

**Get one student — `GET /students/{id}`**

1. Expand **GET /students/{id}** and click **Try it out**.
2. Enter `1` in the **id** field and click **Execute**.
3. Response code **200** with the student, or **404** if the id does not exist.

**Update a student — `PUT /students/{id}`**

1. Expand **PUT /students/{id}** and click **Try it out**.
2. Enter `1` in the **id** field and set the request body:

   ```json
   {
     "name": "Priya",
     "course": "Java"
   }
   ```

3. Click **Execute**. Response code **200** with the updated student, or **404** if the id does not exist.

**Delete a student — `DELETE /students/{id}`**

1. Expand **DELETE /students/{id}** and click **Try it out**.
2. Enter `1` in the **id** field and click **Execute**.
3. Response code **204** (deleted, no body), or **404** if the id does not exist.

Each response in Swagger also shows the equivalent **curl** command, which you can copy into a terminal.

## curl examples

```bash
curl http://localhost:8080/name
curl -X POST http://localhost:8080/students -H "Content-Type: application/json" -d '{"name":"Priya","course":"DevOps"}'
curl http://localhost:8080/students
curl http://localhost:8080/students/1
curl -X PUT http://localhost:8080/students/1 -H "Content-Type: application/json" -d '{"name":"Priya","course":"Java"}'
curl -X DELETE http://localhost:8080/students/1
```

## Build

```bash
mvn clean package
java -jar target/devops-java-demo-1.0.0.jar
```

## Troubleshooting: port 8080 already in use

If startup fails with `Port 8080 was already in use`, find and stop the process holding the port (Windows):

```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Replace `<PID>` with the number in the last column of the `netstat` output, then start the app again.
