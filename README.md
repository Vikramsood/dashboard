# AeroOps Dashboard

## Folders
- `frontend-angular`: Angular 18 responsive dashboard.
- `backend-springboot`: Spring Boot 3 REST API (Java 17).
- `database`: pgAdmin-ready PostgreSQL schema and seed data.

## Run
1. In pgAdmin, connect to the default `postgres` database and execute `database/00_create_database.sql` once.
2. Connect pgAdmin to the new `aeroops_dashboard` database, then execute `database/01_create_schema_and_seed.sql`.
3. Update the PostgreSQL username/password in `backend-springboot/src/main/resources/application.properties`.
4. `cd backend-springboot; mvn spring-boot:run`
5. `cd frontend-angular; npm install; npm start`

Frontend opens at `http://localhost:4200`; API endpoints are `/api/dashboard/flights` and `/api/dashboard/summary` on port 8080.
