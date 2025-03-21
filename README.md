

# Setup Database

## Database queries for initial setup

```
create user 'sarvesh'@'localhost';
create database userservice;
grant all privileges on userservice.* to 'sarvesh'@'localhost';
```


# User API Documentation

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

- A new client can be registered with the OAuth2 server by running the test `addRegisterSampleClient` available in the `ScalerCapstoneUserApplicationTests` class, located in `src/test/java/com/scaler/capstone/user/ScalerCapstoneUserApplicationTests.java` by providing `ClientId`, `ClientSecret`, `redirectUri`, `postLogoutRedirectUri`. Provide client secret by encrypting it using Bcrypt.

## Endpoints

### 1. User Signup (Public)

**Endpoint:**

```
POST /users/signup
```

**Description:** Creates a new user. **Request Body:**

```json
{
  "email": "sarvesh@scaler.com",
  "password": "sarvesh123",
  "name": "Sarvesh Nerurkar",
  "street": "ABC Main Road",
  "city": "Mumbai",
  "state": "Maharashtra",
  "zipcode": "400081",
  "country": "INDIA",
  "roles": ["USER"],
  "resetPasswordQuestion": "What is your pet's name?",
  "resetPasswordAnswer": "Casper"
}
```

**Response:**

```json
{
  "id": 1,
  "email": "sarvesh@scaler.com",
  "name": "Sarvesh Nerurkar",
  "roles": ["USER"]
}
```

**Response Code:** `201 Created`

---

### 2. Get All Users (Requires Authentication)

**Endpoint:**

```
GET /users/getallusers
```

**Description:** Fetches all users (Only accessible by `SUPER_ADMIN`). **Response:**

```json
[
  {
    "id": 1,
    "email": "sarvesh@scaler.com",
    "name": "Sarvesh Nerurkar",
    "roles": ["USER"]
  }
]
```

**Response Code:** `200 OK`

---

### 3. Get User by Email (Requires Authentication)

**Endpoint:**

```
GET /users/getuser/{email}
```

**Description:** Fetches a user by their email (Only accessible to the user themselves). **Response:**

```json
{
  "id": 1,
  "email": "sarvesh@scaler.com",
  "name": "Sarvesh Nerurkar",
  "roles": ["USER"]
}
```

**Response Code:** `200 OK`

---

### 4. Get Reset Password Question (Public)

**Endpoint:**

```
GET /users/getresetpasswordquestion/{email}
```

**Description:** Returns the reset password security question for a user. **Response:**

```json
{
  "resetPasswordQuestion": "What is your pet's name?"
}
```

**Response Code:** `200 OK`

---

### 5. Reset Password (Public)

**Endpoint:**

```
POST /users/resetpassword
```

**Description:** Resets the user's password. **Request Body:**

```json
{
  "email": "sarvesh@scaler.com",
  "resetPasswordQuestion": "What is your pet's name?",
  "resetPasswordAnswer": "Casper",
  "newPassword": "newsarvesh123"
}
```

**Response:**

```json
{
  "id": 1,
  "email": "sarvesh@scaler.com",
  "name": "Sarvesh Nerurkar",
  "roles": ["USER"]
}
```

**Response Code:** `200 OK`

---

### 6. Update User Details (Requires Authentication)

**Endpoint:**

```
PATCH /users/updateuser/{id}
```

**Description:** Updates the details of a user (Only accessible to the user themselves). **Request Body:**

```json
{
  "name": "Sarvesh Nerurkar",
  "city": "Pune"
}
```

**Response:**

```json
{
  "id": 1,
  "email": "sarvesh@scaler.com",
  "name": "Sarvesh Nerurkar",
  "city": "Pune"
}
```

**Response Code:** `200 OK`

---

### 7. Add Role to User (Requires Authentication)

**Endpoint:**

```
PATCH /users/addrole/{id}
```

**Description:** Adds a role to the user (Only accessible to the user themselves). **Query Parameter:**

```
roleName=ADMIN
```

**Response:**

```json
{
  "id": 1,
  "email": "sarvesh@scaler.com",
  "roles": ["USER", "ADMIN"]
}
```

**Response Code:** `200 OK`

---

### 8. Remove Role from User (Requires Authentication)

**Endpoint:**

```
PATCH /users/removerole/{id}
```

**Description:** Removes a role from the user (Only accessible to the user themselves). **Query Parameter:**

```
roleName=ROLE_ADMIN
```

**Response:**

```json
{
  "id": 1,
  "email": "sarvesh@scaler.com",
  "roles": ["USER"]
}
```

**Response Code:** `200 OK`

---

### 9. Delete User (Requires Authentication)

**Endpoint:**

```
DELETE /users/deleteuser/{email}
```

**Description:** Deletes a user account (Only accessible to the user themselves). **Response Code:** `200 OK`

---

## Security & Authentication

- **OAuth 2.0 with JWT authentication required**
- **Public Endpoints:**
    - `/users/signup`
    - `/users/resetpassword`
    - `/users/getresetpasswordquestion/{email}`
- **Authentication Required:**
    - `/users/getuser/**`
    - `/users/updateuser/**`
    - `/users/addrole/**`
    - `/users/removerole/**`
    - `/users/deleteuser/**`

## Error Responses

- **400 Bad Request:** Invalid request format.
- **401 Unauthorized:** Invalid or missing JWT token.
- **403 Forbidden:** User does not have permission.
- **404 Not Found:** User not found.
- **500 Internal Server Error:** Unexpected server error.

---

### Author

*API developed for user management with OAuth 2.0 authentication, role-based access control using JWT tokens, and database management using Spring Hibernate with Flyway migrations.*

