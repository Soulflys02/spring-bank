# Spring Bank

[![Backend](https://github.com/Soulflys02/spring-bank/actions/workflows/backend.yml/badge.svg)](https://github.com/Soulflys02/spring-bank/actions/workflows/backend.yml)
[![Frontend](https://github.com/Soulflys02/spring-bank/actions/workflows/frontend.yml/badge.svg)](https://github.com/Soulflys02/spring-bank/actions/workflows/frontend.yml)

A fictional online bank, built as a learning project. Customers open accounts, deposit money and send transfers, and every movement is recorded in a double-entry ledger.

> Spring Bank is not a real bank and is not affiliated with any existing bank.

## Stack

- **Backend:** Java 25, Spring Boot 4.1, Spring Data JPA, Flyway, PostgreSQL
- **Frontend:** Angular 22, Angular Material
- **Authentication:** Keycloak (planned)
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

Spring Boot starts PostgreSQL through Docker Compose, using `compose.yaml` at the repository root. The API listens on http://localhost:8080. Its endpoints are secured by Spring Security defaults until Keycloak is wired in.

### Frontend

```bash
cd frontend
nvm use
npm install
npm start
```

The app runs on http://localhost:4200. Calls to `/api` are proxied to the backend.

## Project structure

```
spring-bank/
├── backend/       Spring Boot API
├── frontend/      Angular app
└── compose.yaml   Local services (PostgreSQL)
```
