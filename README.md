# ContactPro – Intelligent Relationship Management System

## 📌 Overview

ContactPro is a web-based relationship management backend system built using **Spring Boot** and **PostgreSQL**.

It goes beyond traditional contact storage by enabling structured contact management, follow-up tracking, task management, and relationship analytics.

This project is developed as part of an internship and backend engineering learning journey.

---

## 🚀 Tech Stack

### Backend
- Java 17+
- Spring Boot 4
- Spring Data JPA (Hibernate)
- Spring Security (JWT – upcoming)
- Maven

### Database
- PostgreSQL

### API Testing
- Postman / Thunder Client

---

## 🏗 Architecture

The project follows a clean layered architecture:

Controller → Service → Repository → Database


- **Controller** → Handles HTTP requests
- **Service** → Contains business logic
- **Repository** → Handles database operations
- **Entity** → Maps Java classes to database tables
- **DTO** → Used for secure API communication

---

## 📂 Project Structure

com.contactpro.contactpro
├── controller
├── service
├── repository
├── model
├── dto
├── config
├── security
├── exception
└── ContactproApplication.java


---

## ✅ Features (Current Phase)

### 👤 User Module
- Create User
- Get All Users
- Entity-based table creation
- DTO-based API structure
- Layered architecture implementation

### 📇 Contact Module (In Progress)
- One-to-Many relationship with User
- Contact entity with foreign key mapping
- Category, notes, block/favorite flags

---

## 🗄 Database Schema

### Users Table
- `id` (Primary Key)
- `name`
- `email` (Unique)
- `password`
- `created_at`

### Contacts Table
- `id` (Primary Key)
- `user_id` (Foreign Key)
- `name`
- `phone`
- `email`
- `category`
- `notes`
- `is_blocked`
- `is_favorite`
- `created_at`
