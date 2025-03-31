

# Setup

## Please refer project-documentation repository for entire setup.

## Base URL

```
http://localhost:9001/users
```

## Authentication

This API uses **OAuth 2.0 with JWT tokens** for authentication. Clients must include a **Bearer Token** in the `Authorization` header for protected endpoints.

**Sample Header:**

```
Authorization: OAuth2.0 token
```

## Database and Migrations
- This service utilizes **Spring Hibernate** for database queries, ensuring efficient ORM-based interactions.
- **Flyway** manages database schema migrations, enabling version-controlled updates and rollback support. 
- Database changes are handled through Flyway migration scripts stored in the `db/migration` folder, ensuring smooth and structured updates without data loss. 
- Schema updates are automatically applied at application startup, maintaining consistency across environments (Just need to create database separately as mentioned in start of doc).

## Registering a New Client with OAuth2 Server

- Flyway V5_clientsetup.sql automatically adds one client to the database. Client Id is `scaler` and Client Secret is `scaler-secret` 

## Endpoints

## Endpoints

### Please refer `API Documentation` and `Postman Collection` Folder in `project-documentation` repository

### Author

*API developed for user management with OAuth 2.0 authentication, role-based access control using JWT tokens, and database management using Spring Hibernate with Flyway migrations.*

