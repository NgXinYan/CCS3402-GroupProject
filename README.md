# Room Rental Management System

Welcome to the **Room Rental Management System**! This application is designed to help owners list their properties for rent, while allowing tenants to easily browse available rooms, save them to a wishlist, and book viewing appointments.

## Features

- **User Authentication**: Secure sign-up, login, and profile management for all users.
- **Role Flexibility**: Users can act as both **Owners** (managing listings) and **Tenants** (browsing and booking).
- **Room Management (Owner)**:
  - Create and manage room listings with comprehensive details (e.g., price, location, facilities like WiFi and Air Cond).
  - Track room status (Available vs. Rented).
- **Browsing & Wishlist (Tenant)**:
  - Explore a dashboard of available rooms.
  - Save interesting properties to a personal wishlist for quick access.
- **Viewing Appointments**:
  - Tenants can request viewing appointments for specific dates and times.
  - Owners can approve, reject, or suggest new times for the appointments.
  
## Technology Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Frontend**: Thymeleaf, HTML5, CSS
- **Database**: Oracle Database
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven

## Local Setup Instructions

Follow these steps to run the application on your local machine:

### 1. Prerequisites
- Java Development Kit (JDK) 17 installed.
- Maven installed.
- Access to an Oracle Database.
- A Gmail account with an "App Password" generated (for sending email notifications).

### 2. Clone the Repository
```bash
git clone <repository-url>
cd CCS3402-GroupProject
```

### 3. Configure the Application
Open `src/main/resources/application.properties` and update the following settings to match your local environment:

**Database Configuration**:
Replace the URL, username, and password with your Oracle Database credentials.
```properties
spring.datasource.url=jdbc:oracle:thin:@<your-oracle-db-host>:<port>:<sid>
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

### 4. Run the Application
You can run the application using Maven:
```bash
mvn spring-boot:run
```
Alternatively, if you are using the Maven wrapper:
- **Windows**: `mvnw.cmd spring-boot:run`
- **Mac/Linux**: `./mvnw spring-boot:run`

### 5. Access the Application
Once the application starts successfully, open your web browser and navigate to:
```
http://localhost:8080
```

### 6. Default Accounts
There are some accounts that are added in default for demonstration purposes:
1. email: user1@email.com, pw: user1 (as owner)
2. email: user2@email.com, pw: user2 (as tenant)
3. email: user3@email.com, pw: user3 (use to check crash time and date for appointment)
