# Spring Bank

[![Backend](https://github.com/Soulflys02/spring-bank/actions/workflows/backend.yml/badge.svg)](https://github.com/Soulflys02/spring-bank/actions/workflows/backend.yml)
[![Frontend](https://github.com/Soulflys02/spring-bank/actions/workflows/frontend.yml/badge.svg)](https://github.com/Soulflys02/spring-bank/actions/workflows/frontend.yml)

A fictional online bank, built as a learning project. Customers open accounts, deposit money and send transfers, and every movement is recorded in a double-entry ledger.

> Spring Bank is not a real bank and is not affiliated with any existing bank.

## Stack

- **Backend:** Java 25, Spring Boot 4.1, Spring Data JPA, Flyway, PostgreSQL
- **Frontend:** Angular 22, Angular Material
- **Authentication:** Keycloak 26
- **Tests:** JUnit 5, Testcontainers, Vitest

## Status

Work in progress. Features are built one at a time, following the customer journey: sign up, open an account, check the balance, deposit, transfer.

## Getting started

### Prerequisites

- Java 25
- Docker, running
- Node 24 (an `.nvmrc` file is provided for nvm)

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

Spring Boot starts PostgreSQL and Keycloak through Docker Compose, using `compose.yaml` at the repository root. The API listens on http://localhost:8080.

The API documentation is available at http://localhost:8080/swagger-ui.html. Every other endpoint requires authentication, which Keycloak will provide.

### Frontend

```bash
cd frontend
nvm use
npm install
npm start
```

The app runs on http://localhost:4200. Calls to `/api` are proxied to the backend.

### Keycloak

The admin console runs on http://localhost:8081 (`admin` / `admin`). On first start, Keycloak imports the `spring-bank` realm from `keycloak/spring-bank-realm.json`.

Test accounts:

| Username | Password | Roles |
|---|---|---|
| `backoffice` | `backoffice` | `backoffice`, `client` |

After changing the realm in the admin console, export it back into the repository and commit the file:

```bash
docker compose exec keycloak /opt/keycloak/bin/kc.sh export --dir /opt/keycloak/data/import --realm spring-bank --users realm_file
```

To start again from the exported file, remove the containers and their data with `docker compose down -v`.

## Project structure

```
spring-bank/
├── backend/       Spring Boot API
├── frontend/      Angular app
├── keycloak/      Exported Keycloak realm
└── compose.yaml   Local services (PostgreSQL, Keycloak)
```
