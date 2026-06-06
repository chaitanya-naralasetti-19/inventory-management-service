# Project Overview
This application provides REST APIs for managing and searching inventory records within an Inventory Management System. The solution is built using Java 17 and Spring Boot 3.5.14, following clean architecture principles, proper validation, exception handling, and scalable design practices.
## Problem Statement
We have to implement a REST API for Searching inventory records. The API should allow clients to search for inventory items based on various criteria such as item name, category, price range, and availability and any search catogory. The system should be designed to handle a large number of inventory records efficiently and provide accurate search results.

## Assumptions

1. Assuming the Inventory records are stored in a relational database.
2. Assuming the search criteria can be combined (e.g., search by name and category simultaneously).
3. Assuming the API should support pagination for large result sets.

---

## Architecture

## 1. Retrieve Inventory (Paginated Search API)

```mermaid
sequenceDiagram
    autonumber

    actor Client
    participant Controller as InventoryController
    participant Service as InventoryService
    participant Spec as InventorySpecification
    participant Repo as InventoryRepository
    participant DB as PostgreSQL

    Client->>Controller: GET /api/v1/inventory/search<br/>?category=Electronics<br/>&seller=Amazon&page=0&size=10

    alt No Data with Given Filters
        DB-->>Client: HTTP 404 Inventory Not Found
    else Valid Request
        Controller->>Service: searchInventories(searchRequest)

        Service->>Spec: Build dynamic search criteria

        Spec-->>Service: JPA Specification

        Service->>Repo: findAll(specification, pageable)

        Repo->>DB: Execute dynamic SQL query

        DB-->>Repo: Matching inventory records

        Repo-->>Service: Paginated inventory list

        Service-->>Controller: InventoryResponse

        Controller-->>Client: HTTP 200 OK + Paginated Results
    end
```
---

## High Level Design

### Components

- DTO's
    - InventoryRequestDTO
    - InventoryResponseDTO
- Entities
  - Inventory
- Specification(filter)
  - InventorySpecification
- Controller
  - InventoryController
- Service
  - InventoryService
- Repository
  - InventoryRepository
- Database
    - PostgreSQL
---

## API Endpoints

| Method | Endpoint                                         | Description |
|----------|--------------------------------------------------|----------|
| GET | /api/v1/inventory/search?category=Electronics&.. | Retrieve paginated inventory records |

---

## Database Design

## Database Design

```mermaid
erDiagram

    INVENTORY {
        BIGINT id PK
        VARCHAR name
        VARCHAR category
        VARCHAR sub_category
        VARCHAR model
        TEXT specification
        VARCHAR seller
        VARCHAR location
        NUMERIC price
        INTEGER stock
        DATE manufacturing_date
        DATE expiry_date
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }
```

### Tables

| Table     | Purpose |
|-----------|---------|
| Inventory | Stores inventory information |

---

## Validation Strategy

- Bean Validation
- Input Validation

---

## Error Handling

- Global Exception Handler

---

## Pagination

- Offset Pagination

---

## Testing

- Unit Tests for Service Layer and Utility Classes

---

## Future Improvements

1. We can Use JWT for authentication and authorization.
2. We can use Redis for caching frequently accessed inventory records to improve search performance.
3. We can implement advanced search features such as fuzzy search or full-text search using Elasticsearch.

---

## Running Application

### Local

```bash
mvn spring-boot:run