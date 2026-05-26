# Assignment Management API

A fully deployed Spring Boot backend application for managing courses and assignments.  
Built with layered architecture, DTO mapping, JPA relationships, pagination, filtering, and scheduled task handling.

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA / Hibernate
- H2 Database (in-memory for demo)
- Maven
- REST API
- Render Deployment

---

## Features

- CRUD operations for assignments and courses
- Pagination for efficient handling of large datasets
- Filtering assignments by status
- Scheduled task that automatically marks overdue assignments every 5 minutes
- DTO mapping for clean API responses
- One-to-Many relationship between `Course` and `Assignment`
- Global exception handling
- `JOIN FETCH` query optimization to prevent N+1 problems

---

## Live Demo

### Assignments Endpoint
[https://assignmentmanager-7qta.onrender.com/assignments](https://assignmentmanager-7qta.onrender.com/swagger-ui/index.html)

Returns all assignments in JSON format.

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/assignments` | Get all assignments |
| GET | `/assignments/{id}` | Get assignment by ID |
| GET | `/assignments/overdue` | Get overdue assignments |
| GET | `/assignments/status/{status}` | Filter assignments by status |
| POST | `/assignments` | Create assignment |
| PUT | `/assignments/{id}` | Update assignment |
| DELETE | `/assignments/{id}` | Delete assignment |

### Available Status Values

- `TODO`
- `IN_PROGRESS`
- `DONE`
- `OVERDUE`

---

## Run Locally

### Clone Repository

```bash
git clone <your-github-repo-url>
cd <repo-folder>
