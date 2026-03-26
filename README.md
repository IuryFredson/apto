# Apto

Plataforma de moradias universitárias.

## Estrutura

- backend/ → API Spring Boot
- frontend/ → aplicação web
- docker/ → serviços (PostgreSQL, etc)

## Tecnologias

- Java 21 + Spring Boot
- PostgreSQL
- Docker
- React (futuro)

## Como rodar o projeto

### 1. Subir o banco de dados

```bash
cd docker
docker compose up -d
```
###  2. Rodar o backend

```bash
cd backend/apto-api
./mvnw spring-boot:run
```

A aplicação estará disponível em **http://localhost:8080**

## Configuração 

O backend já está configurado para conectar no PostgreSQL local:

- Database: `apto`
- User: `apto`
- Password: `apto`

## Status

- Em desenvolvimento (Sprint 1)

