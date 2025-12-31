# Gym App

A Spring Boot application for managing gym trainees and trainers.

---

## Features

- User registration and login with JWT authentication  
- Brute-force attack protection (blocks IP for 5 minutes after 3 failed login attempts)  
- Passwords securely hashed with BCrypt
- JWT token validation for all secured endpoints  
- Spring Security integrated with JWT
- CORS configured for frontend communication  
- Postman documentation: https://www.postman.com/descent-module-saganist-36810950/gym-application/overview

---

## Technologies Used

- Java 21  
- Spring Boot 3.5  
- Spring Security 6.1  
- JWT (`io.jsonwebtoken`)  
- Hibernate 6 & JPA  
- PostgreSQL  
- Google Guava (for caching login attempts)  
- Lombok  
- H2 Database (for testing)  
- Gradle  
