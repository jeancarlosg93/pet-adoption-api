# Pet Adoption REST API Documentation V2

## Table of Contents
1. [Overview](#overview)
2. [System Architecture](#system-architecture)
3. [API Security](#api-security)
4. [API Endpoints](#api-endpoints)
5. [Data Models](#data-models)
6. [Business Processes](#business-processes)
7. [Dependencies](#dependencies)
8. [Deployment](#deployment)

## Overview
The Pet Adoption REST API is a Spring Boot application that manages pet adoption data and foster care assignments. It provides secure endpoints for managing pets, fosters, and API keys, supporting JSON responses with OpenAPI documentation.

### Key Features
- Pet management (CRUD operations)
- Foster care management
- API key authentication
- OpenAPI/Swagger documentation
- Docker containerization
- In-memory H2 database

## System Architecture
The system follows a layered Spring Boot architecture with the following components:

### Core Components
- Controllers: Handle HTTP requests
    - `PetController`: Pet management endpoints
    - `FosterController`: Foster management endpoints
    - `ApiKeyController`: API key management
- Services: Business logic layer
    - `PetService`: Pet-related operations
    - `FosterService`: Foster-related operations
    - `ApiKeyService`: API key operations
- Repositories: Data access layer
    - `PetRepository`: Pet data operations
    - `FosterRepository`: Foster data operations
    - `ApiKeyRepository`: API key storage

### Technologies Used
- Spring Boot 3.3.5
- Spring Data JPA
- H2 Database
- OpenAPI 3.0
- Docker
- Java 21

## API Security
The API uses a custom API key authentication system:

- All endpoints except `/api/v1/keys/generate` require an API key
- API keys are sent via the `X-API-KEY` header
- Keys can be configured with expiration dates
- Keys can be revoked or cleaned up automatically

## API Endpoints

### Pet Management
```
GET    /api/v1/pets            - Get all pets
GET    /api/v1/pets/{id}       - Get pet by ID
GET    /api/v1/pets/available  - Get available pets
GET    /api/v1/pets/species/{species} - Get pets by species
POST   /api/v1/pets            - Create new pet
PUT    /api/v1/pets/{id}       - Update pet
PUT    /api/v1/pets/{id}/status - Update pet status
DELETE /api/v1/pets/{id}       - Remove pet
```

### Foster Management
```
GET    /api/v1/fosters            - Get all fosters
GET    /api/v1/fosters/{id}       - Get foster by ID
GET    /api/v1/fosters/active     - Get active fosters
GET    /api/v1/fosters/available  - Get available fosters
POST   /api/v1/fosters            - Create new foster
PUT    /api/v1/fosters/{id}       - Update foster
DELETE /api/v1/fosters/{id}       - Deactivate foster
POST   /api/v1/fosters/{fosterId}/pets/{petId} - Assign pet to foster
DELETE /api/v1/fosters/{fosterId}/pets/{petId} - Remove pet from foster
```

### API Key Management
```
POST   /api/v1/keys/generate - Generate new API key
GET    /api/v1/keys         - List all API keys
DELETE /api/v1/keys/{id}    - Revoke API key
POST   /api/v1/keys/cleanup - Clean up expired keys
```

## Data Models

### Pet
```java
public class Pet {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private String temperament;
    private int age;
    private String gender;
    private double weight;
    private String color;
    private LocalDateTime dateArrived;
    private double adoptionFee;
    private Foster currentFoster;
    private Status currentStatus; // FOSTERED, AVAILABLE, ADOPTED, REMOVED
}
```

### Foster
```java
public class Foster extends User {
    private Instant fosterSince;
    private boolean active;
    private int maxPets;
    private List<Pet> petsAssigned;
}
```

### ApiKey
```java
public class ApiKey {
    private Long id;
    private String keyValue;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant expiresAt;
    private String createdBy;
}
```

## Business Processes

1. Pet Management
    - Pet registration with detailed information
    - Status tracking (Available, Fostered, Adopted, Removed)
    - Foster assignment management
    - Adoption fee tracking

2. Foster Management
    - Foster registration and verification
    - Pet capacity management
    - Active/Inactive status tracking
    - Pet assignment tracking

3. API Key Management
    - Key generation with optional expiration
    - Key validation and revocation
    - Automatic cleanup of expired keys

## Dependencies
Key dependencies from pom.xml:
```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- springdoc-openapi-starter-webmvc-ui
- h2database
- lombok
- spring-boot-starter-test
```

## Deployment
The application can be deployed using Docker:

1. Build the Docker image:
```bash
docker compose build
```

2. Run the container:
```bash
docker compose up
```

3. Access the application:
- API Documentation: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
- Web Interface: http://localhost:8080/index.html

### Environment Configuration
Key configuration in application.properties:
```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:petdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

### Docker Configuration
The application is containerized using a multi-stage build process:
- Build stage: Uses eclipse-temurin:21-jdk-alpine
- Runtime stage: Uses eclipse-temurin:21-jre-alpine
- Includes health checks and resource limits
- Uses non-root user for security