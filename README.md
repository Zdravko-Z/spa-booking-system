Serenity Spa Booking System
A full‑stack web application for managing spa treatments, bookings, rooms, and staff. Built with Spring Boot and Thymeleaf, it features role‑based access for guests, staff, and administrators.

Tech Stack
Java 17

Spring Boot 3.4.0

Spring MVC + Thymeleaf

Spring Data JPA

Spring Security Crypto (BCrypt password hashing)

PostgreSQL

Flyway

Lombok

Maven

Features
Public (no login required)
Home page – displays all active treatments.

Treatment browsing – view treatment name, duration and price.

Guest / Client (logged‑in)
Registration & Login – secure account creation with BCrypt‑hashed passwords.

Profile management – update first name, last name, and phone number.

Book a treatment – two‑step booking flow:

Pick a date – view available time slots.
Select treatments, optional therapist, and notes.
My Bookings – view all upcoming and past bookings.

Cancel bookings – cancel pending bookings (future dates only).

Staff
Therapist assignment – staff appear as optional selectors during booking.

Staff management – edit staff names and specialisations (admin only).

Admin (full control)
Admin Dashboard – overview of all bookings with quick links.

Booking management – cancel any booking from the dashboard.

Treatment management – create, edit, delete, and restore treatments (soft delete).

Room management – edit room name, capacity, and status.

Staff management – edit staff names and specialisations.

Security & Access Control
Session‑based authentication – stores user_id and user_role in HttpSession.

Role‑based access – CLIENT, STAFF, ADMIN roles.

Interceptor – protects /admin routes and redirects unauthenticated users.

Guest‑only booking – only clients with a guest profile can book treatments.

Database Schema
The database is managed via Flyway migrations (versioned SQL scripts).
7 core tables:

Table	Description
users	Authentication and role data
guests	Client profile data (linked to users)
spa_staff	Staff profile data (linked to users)
spa_rooms	Spa rooms with capacity and status
spa_treatments	Treatment details with soft‑delete flag
spa_bookings	Booking records with price snapshot
spa_booking_treatments	Junction table linking bookings and treatments (price snapshot)
All primary keys use UUID (database‑agnostic).

