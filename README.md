# School Management System — Backend

A REST API for managing a school: users and roles, education terms, lessons and lesson programs, meetings, and student grades. Built with **Spring Boot** and secured with **JWT** and role-based authorization.

---

## Features

- **JWT authentication** with stateless, token-based login
- **Role-based access control** across five roles: Admin, Dean, Vice-Dean, Teacher, Student
- **User management** — create, update, delete, and page through users by role
- **Education terms** — define semesters with start/end and registration dates
- **Lessons & lesson programs** — create lessons and assign them to teachers and students on a weekly schedule
- **Meetings** — teachers schedule meetings with their advisory students
- **Student info & grades** — record midterm/final scores with a configurable weighting and letter grades
- Bean validation on request payloads, paginated list endpoints, and centralized error messages
- A default **SuperAdmin** account is bootstrapped automatically on first run

---

## Tech Stack

| Concern         | Technology                          |
|-----------------|-------------------------------------|
| Language        | Java 11                             |
| Framework       | Spring Boot 2.6.3                   |
| Security        | Spring Security + JWT (jjwt 0.9.1)  |
| Persistence     | Spring Data JPA (Hibernate)         |
| Database        | PostgreSQL                          |
| Validation      | Spring Boot Starter Validation      |
| Boilerplate     | Lombok                              |
| Build tool      | Maven (wrapper included)            |

---

## Project Structure

```
src/main/java/com/project/schoolmanagment/
├── SchoolManagmentSystemApplication.java   # entry point + bootstrap of roles & SuperAdmin
├── controller/
│   ├── user/          # Authentication, User, Student, Teacher
│   └── buisnes/       # EducationTerm, Lessons, LessonProgram, Meeting, StudentInfo
├── service/           # business logic (user, buisnes, helper, validator)
├── repository/        # Spring Data JPA repositories
├── entity/
│   ├── concretes/     # User, UserRole + business entities
│   └── enums/         # RoleType, Day, Term, Gender, Note
├── payload/
│   ├── request/       # incoming DTOs
│   ├── response/      # outgoing DTOs
│   ├── mappers/       # entity ⇄ DTO mapping
│   └── messages/      # success & error message constants
├── security/
│   ├── config/        # WebSecurityConfig
│   ├── jwt/           # token filter, utils, entry point
│   └── service/       # UserDetails implementations
└── exception/         # custom exceptions
```

---

## Roles

| Role              | Authority name |
|-------------------|----------------|
| Admin             | `Admin`        |
| Dean              | `Dean`         |
| Vice-Dean         | `ViceDean`     |
| Teacher           | `Teacher`      |
| Student           | `Student`      |

Endpoints are protected with `@PreAuthorize`, so access depends on the authenticated user's role.

---

## Prerequisites

- **JDK 11** or later
- **PostgreSQL** running locally (or reachable over the network)
- Maven is **not** required — the project ships with the Maven wrapper (`./mvnw`)

---

## Configuration

Configuration lives in `src/main/resources/application.properties`. The important values:

```properties
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/School_Management_System
spring.datasource.username=dev_user
spring.datasource.password=admin

spring.jpa.hibernate.ddl-auto=update

backendapi.app.jwtSecret=sdfsdfsdfs
backendapi.app.jwtExpirationMs=8640000

midterm.exam.impact.percentage=0.40
final.exam.impact.percentage=0.60
```

Before running:

1. Create the database, e.g.:

   ```sql
   CREATE DATABASE "School_Management_System";
   ```

2. Update `spring.datasource.username` / `spring.datasource.password` to match your PostgreSQL setup.
3. Change `backendapi.app.jwtSecret` to a strong, private value — the checked-in value is a placeholder and must not be used outside local development.

> Tip: for anything beyond local work, keep the database password and JWT secret out of the file and supply them as environment variables or command-line arguments instead.

---

## Running the App

Schema tables are created/updated automatically by Hibernate (`ddl-auto=update`).

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Or build a jar and run it:

```bash
./mvnw clean package
java -jar target/School-Managment-System-0.0.1-SNAPSHOT.jar
```

The API starts on **http://localhost:8080**.

### Default admin account

On the very first startup (when no users exist yet), the five roles and a default administrator are created:

| Field    | Value          |
|----------|----------------|
| Username | `SuperAdmin`   |
| Password | `München24*`   |

Use it to log in and create the first real users, then change or remove it.

---

## Authentication Flow

1. `POST /auth/login` with a username and password returns a JWT.
2. Send the token on every protected request via the header:

   ```
   Authorization: Bearer <token>
   ```

3. Spring Security validates the token and enforces the caller's role on each endpoint.

---

## API Overview

Base URL: `http://localhost:8080`

### Auth — `/auth`

| Method | Path               | Description                        |
|--------|--------------------|------------------------------------|
| POST   | `/login`           | Log in, receive a JWT              |
| PATCH  | `/updatePassword`  | Change the current user's password |
| GET    | `/user`            | Get the current authenticated user |

### Users — `/user`

| Method | Path                              | Description                       |
|--------|-----------------------------------|-----------------------------------|
| POST   | `/save/{userRole}`                | Create a user with a given role   |
| GET    | `/getAllUsersByPage/{userRole}`   | Page users by role (Admin only)   |
| GET    | `/getUserById/{userId}`           | Get a user by id                  |
| GET    | `/getUserByName`                  | Search users by name              |
| PATCH  | `/updateUser`                     | Update the current user           |
| PUT    | `/update/{userId}`                | Update a user by id               |
| DELETE | `/delete/{userId}`                | Delete a user                     |

### Students — `/student`

| Method | Path                            | Description                                   |
|--------|---------------------------------|-----------------------------------------------|
| POST   | `/save`                         | Create a student (Admin)                      |
| POST   | `/addLessonProgramToStudent`    | Student picks a lesson program                |
| POST   | `/update`                       | Student updates own profile                   |
| PUT    | `/update/{id}`                  | Update a student (Admin/Dean/ViceDean)        |
| GET    | `/changeStatus`                 | Toggle a student's active status              |

### Teachers — `/teacher`

| Method | Path                                | Description                              |
|--------|-------------------------------------|------------------------------------------|
| POST   | `/save`                             | Create a teacher (Admin)                 |
| POST   | `/addLessonProgram`                 | Assign a lesson program to a teacher     |
| PUT    | `/update/{userId}`                  | Update a teacher                         |
| GET    | `/getAllAdvisorTeacher`             | List advisor teachers                    |
| GET    | `/getAllStudentByAdvisorUsername`   | List a teacher's advisory students       |
| GET    | `/deleteAdvisorTeacherById/{id}`    | Remove an advisor teacher                |

### Education Terms — `/educationTerms`

| Method | Path                             | Description                    |
|--------|----------------------------------|--------------------------------|
| POST   | `/save`                          | Create an education term       |
| GET    | `/getAll`                        | List all terms                 |
| GET    | `/{id}`                          | Get a term by id               |
| GET    | `/getAllEducationTermsByPage`    | Paged list of terms            |
| PUT    | `/update/{id}`                   | Update a term                  |
| DELETE | `/delete/{id}`                   | Delete a term                  |

### Lessons — `/lessons`

| Method | Path                     | Description                |
|--------|--------------------------|----------------------------|
| POST   | `/save`                  | Create a lesson            |
| POST   | `/updateLesson/{id}`     | Update a lesson            |
| GET    | `/getLessonByName`       | Find a lesson by name      |
| GET    | `/findLessonByPage`      | Paged list of lessons      |
| GET    | `/findLessonBySet`       | Fetch lessons by id set    |
| DELETE | `/delete/{id}`           | Delete a lesson            |

### Lesson Programs — `/lessonProgram`

| Method | Path                                          | Description                              |
|--------|-----------------------------------------------|------------------------------------------|
| POST   | `/save`                                       | Create a lesson program                  |
| GET    | `/getAll`                                     | List all programs                        |
| GET    | `/getAllUnassigned`                           | Programs not yet assigned                |
| GET    | `/getAllAssigned`                             | Programs already assigned                |
| GET    | `/getById/{id}`                               | Get a program by id                      |
| GET    | `/findLessonProgramByPage`                    | Paged list                               |
| GET    | `/getAllLessonProgramByTeacher`               | Programs of the current teacher          |
| GET    | `/getAllLessonProgramByTeacherId/{teacherId}` | Programs of a given teacher              |
| GET    | `/getAllLessonProgramByStudentId/{student}`   | Programs of a given student              |
| DELETE | `/delete/{id}`                                | Delete a program                         |

### Meetings — `/meet`

| Method | Path                          | Description                        |
|--------|-------------------------------|------------------------------------|
| POST   | `/save`                       | Schedule a meeting                 |
| POST   | `/update/{meetingId}`         | Update a meeting                   |
| GET    | `/getAll`                     | List all meetings                  |
| GET    | `/getAllByTeacher`            | Meetings of the current teacher    |
| GET    | `/getAllByStudent`            | Meetings of the current student    |
| GET    | `/getAllMeetByPage`           | Paged list                         |
| GET    | `/getAllMeetByPageByTeacher`  | Paged list for a teacher           |
| DELETE | `/delete/{id}`                | Delete a meeting                   |

### Student Info (grades) — `/studentInfo`

| Method | Path                              | Description                        |
|--------|-----------------------------------|------------------------------------|
| POST   | `/save`                           | Add a grade record                 |
| POST   | `/update/{studentInfo}`           | Update a grade record              |
| GET    | `/get/{studentInfoId}`            | Get a record by id                 |
| GET    | `/getByStudentId/{studentId}`     | Records for a student              |
| GET    | `/getAllByTeacher`                | Records created by the teacher     |
| GET    | `/getAllByStudent`                | Records of the current student     |
| GET    | `/getAllStudentInfoByPage`        | Paged list                         |
| DELETE | `/delete/{studentInfo}`           | Delete a record                    |

---

## Grading

A student's final score is a weighted average of the midterm and final exams. The weights are configurable in `application.properties`:

```properties
midterm.exam.impact.percentage=0.40
final.exam.impact.percentage=0.60
```

The numeric average is mapped to a letter grade (`AA`, `BA`, `BB`, `CB`, `CC`, `DC`, `DD`, `DZ`, `FF`).

---

## Notes

- `ddl-auto=update` is convenient for development but is generally discouraged for production — prefer versioned migrations (e.g. Flyway or Liquibase) there.
- The bundled JWT secret and database credentials are placeholders for local use. Replace them and keep real secrets out of version control.

---

## License

No license.
