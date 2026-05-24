Assignment Management API 

A fully deployed Spring Boot backend application for managing courses and assignments. Designed with layered architecture, DTO mapping, JPA relationships, pagination, filtering, and scheduled task handling.


Tech Stack
•	Java 21 
•	Spring Boot 3 
•	Spring Data JPA / Hibernate 
•	H2 Database (in-memory for demo) 
•	Maven 
•	Railway Deployment 
•	REST API



Features
•	Crud operations for the assignment and course entities
•	Pagination is used to retrieve large data sets efficiently 
•	Assignments can be filtered by status
•	Schedules task is responsible for marking overdue assignments every 5 minutes
•	DTO Mapping for clean API responses 
•	One to Many relationship between Course and Assignment 
•	Error handling with global exception handler 
•	JOIN FETCH is used to prevent N+1 problems (query optimization)



Live Demo 
Test the deployed backend endpoints: 
•	https://assignmentmanager-7qta.onrender.com/assignments
•	This endpoint returns all assignments in JSON format.



API Endpoints 

Method	Path	Description
GET	/assignments	Get all assignments
GET	/assignments/{id}	Get a specific assignment by ID
GET	/assignments/overdue	Get overdue assignments
GET	/assignments/status/ {status}	Get assignments filtered by status (TODO, IN_PROGRESS, DONE, OVERDUE)
POST	/assignments	Create a new assignment
PUT	/assignments/{id}	Update an existing assignment
DELETE	/assignments/{id}	Delete an assignment



Run Locally 
Clone the repository: 
•	git clone <your-github-repo-url>
•	cd <repo-folder>
Build and run the project:
•	mvn clean package
•	mvn spring-boot:run
Open the API in Postman or browser:
•	http://localhost:8080/assignments
Optional: H2 console for database access:
•	http://localhost:8080/h2-console
•	JDBC URL: jdbc:h2:mem:testdb 
•	Username: sa 
•	Password: (leave blank)
