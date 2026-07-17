# Dibs - Lost & Found Backend

Dibs is a Spring Boot application that serves as the backend API for a Lost & Found system. It features JWT-based local authentication, Google OAuth2 client authentication, PostgreSQL database storage, and a RESTful API for managing lost and found items.

---

## Tech Stack

- **Framework**: Spring Boot 4.0.6 (Spring Web MVC, Spring Data JPA, Spring Security)
- **Language**: Java 25
- **Database**: PostgreSQL (Production/Dev), H2 Database (Test Profile)
- **Security**: JWT & Google OAuth2
- **Build Tool**: Maven

---

## Prerequisites

Before running this application, make sure you have the following installed:
- **Java Development Kit (JDK) 25** (Eclipse Temurin is recommended)
- **Maven** (or use the provided Maven wrapper `mvnw` / `mvnw.cmd`)
- **PostgreSQL** (running locally or in Docker)
- **Google Cloud Developer Account** (for configuring Google Login OAuth2 credentials)

---

## Environment Variables

The application can be configured using environment variables. These are mapped in [application.properties](src/main/resources/application.properties):

| Environment Variable | Description | Default Value | Required |
| :--- | :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | JDBC Connection URL for PostgreSQL | `jdbc:postgresql://localhost:5432/Dibs_db` | Optional |
| `SPRING_DATASOURCE_USERNAME`| Username for the PostgreSQL database | `admin` | Optional |
| `SPRING_DATASOURCE_PASSWORD`| Password for the PostgreSQL database | `123` | Optional |
| `PORT` | The port the application server listens on | `8081` | Optional |
| `GOOGLE_CLIENT_ID` | Google OAuth2 Client ID | *None* | **Required** for Google Login |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 Client Secret | *None* | **Required** for Google Login |
| `FRONTEND_URL` | Allowed CORS origin for the frontend application | `http://localhost:5173` | Optional |

---

## Database Setup

### Option 1: Using Docker (Recommended)
You can start a PostgreSQL database instance in the background using Docker:
```bash
docker run --name dibs-postgres -e POSTGRES_DB=Dibs_db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123 -p 5432:5432 -d postgres:latest
```

### Option 2: Local PostgreSQL Installation
1. Install PostgreSQL on your machine.
2. Connect to your database server using `psql` or a database GUI client (e.g. pgAdmin, DBeaver).
3. Run the following command to create the database:
   ```sql
   CREATE DATABASE Dibs_db;
   ```
4. Update/set your environment variables `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD` to match your local PostgreSQL credentials if they differ from the defaults (`admin` / `123`).

> [!NOTE]
> Database tables will be automatically created on startup via Hibernate's automatic schema update setting (`spring.jpa.hibernate.ddl-auto=update`).

---

## Google OAuth2 Setup

Google Login is integrated into Spring Security. To get OAuth2 credentials:
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create or select a project.
3. Configure the **OAuth consent screen** (Internal or External).
4. Go to the **Credentials** page, click **Create Credentials**, and select **OAuth client ID**.
5. Select **Web application** as the application type.
6. Set the **Authorized redirect URIs** to:
   - `http://localhost:8081/login/oauth2/code/google`
7. Copy the generated **Client ID** and **Client Secret**.
8. Export them in your terminal environment before running the application:
   - **Linux/macOS**:
     ```bash
     export GOOGLE_CLIENT_ID="your-client-id"
     export GOOGLE_CLIENT_SECRET="your-client-secret"
     ```
   - **Windows (Command Prompt)**:
     ```cmd
     set GOOGLE_CLIENT_ID="your-client-id"
     set GOOGLE_CLIENT_SECRET="your-client-secret"
     ```
   - **Windows (PowerShell)**:
     ```powershell
     $env:GOOGLE_CLIENT_ID="your-client-id"
     $env:GOOGLE_CLIENT_SECRET="your-client-secret"
     ```

---

## Running the Application Locally

### 1. Run JUnit Tests
JUnit tests run using an in-memory H2 database under the `test` profile. There is no need to configure PostgreSQL for tests.
```bash
./mvnw test
```

### 2. Start the Dev Server
To run the Spring Boot application locally:
```bash
./mvnw spring-boot:run
```
The server will start up on port `8081` (unless configured otherwise via `PORT`).

---

## Docker Deployment & Docker Compose

### Building the Docker Image
To build the application's Docker image using the provided [Dockerfile](Dockerfile):
```bash
docker build -t dibs-app .
```

### Using Docker Compose
You can easily spin up the Spring Boot application and the PostgreSQL database together by creating a `docker-compose.yml` file in the root directory:

```yaml
version: '3.8'

services:
  db:
    image: postgres:latest
    container_name: dibs-db
    environment:
      POSTGRES_DB: Dibs_db
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: 123
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  app:
    image: dibs-app
    container_name: dibs-backend
    build: .
    ports:
      - "8081:8081"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/Dibs_db
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: 123
      GOOGLE_CLIENT_ID: ${GOOGLE_CLIENT_ID}
      GOOGLE_CLIENT_SECRET: ${GOOGLE_CLIENT_SECRET}
      FRONTEND_URL: http://localhost:5173
    depends_on:
      - db

volumes:
  pgdata:
```

Run the entire stack with:
```bash
docker compose up -d
```

---

## Key API Endpoints

### Authentication
- `POST /register` - Register a new user (`multipart/form-data` with `user` JSON and optional `profilePicture` file)
- `POST /login` - Local user login (returns JWT token set inside HttpOnly cookie)
- `GET /user` - Retrieve details of the currently logged-in user

### Items Management
- `POST /item` - Add a lost/found item (`multipart/form-data` with `item` JSON and `image` file)
- `POST /allitems` - List all items in the system
- `GET /lostitems` - List all lost items
- `GET /founditems` - List all found items
- `GET /mylostitems` - List lost items posted by the current user
- `GET /myfounditems` - List found items posted by the current user
- `GET /getItem/{id}` - Get details of a specific item by ID
- `GET /search/{name}` - Search items by name containing the query
- `GET /claimitem/{itemid}` - Claim a found/lost item
- `GET /claimedItems` - List items claimed by the current user
- `GET /postedItems` - List items posted by the current user
- `DELETE /deleteItem` - Remove an item by ID (parameter `i`)
