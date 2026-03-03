📝 Multi-User Secure Journal Management Backend (Cloud Deployed)

A secure, production-ready REST API built using Spring Boot for managing multi-user journal entries. The system implements role-based access control (RBAC), user-level authorization, cloud database integration, containerized deployment, and interactive API documentation.

🚀 Live Demo

Swagger UI (Cloud Deployment):

🛠 Tech Stack

Java

Spring Boot

Spring Security (RBAC + BCrypt)

Spring Data MongoDB

MongoDB Atlas (Cloud NoSQL Database)

Docker (Containerized Application)

Render (Cloud Deployment)

Swagger / OpenAPI

Maven

🔐 Security Features

Role-Based Access Control (ADMIN / USER)

BCrypt password hashing

Strict user-level data isolation (prevents cross-user access)

Secured endpoints using Spring Security

Centralized Global Exception Handling for consistent API responses

📌 Core Features

User registration and authentication

Create, update, and delete journal entries

Admin-restricted endpoints

Clean controller–service–repository layered architecture

Interactive API testing using Swagger

Cloud-based MongoDB Atlas integration

🐳 Deployment

The application is containerized using Docker and deployed on Render with environment-based configuration.

Production environment uses:

MONGODB_URI as an environment variable for secure database connection.

▶ Running Locally

Clone the repository
git clone 

Navigate to the project folder
cd journal-app

Configure MongoDB URI inside application-local.yml

Run the application
mvn spring-boot:run

Access Swagger documentation
http://localhost:8080/swagger-ui/index.html

📂 Architecture

The project follows a clean layered architecture:

Controller → Service → Repository

Includes centralized exception handling and proper separation of concerns for maintainability and scalability.

👤 Author

Priyansh Kumar Singh
GitHub: https://github.com/Priyansh011-DEV

LinkedIn: https://www.linkedin.com/in/priyanshsingh01