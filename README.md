# URL Shortener Service

A simple URL shortening service built with Java 21, Spring Boot, and SQLite.

## Features

- Shorten long URLs
- Redirect short URLs to original URLs
- Support custom aliases
- URL validation
- Persistent storage using SQLite
- Idempotent URL shortening (same URL returns same short code)
- 301 redirects
- 404 for unknown short codes
- Automated unit and integration tests

---

## Design Decisions

### Short Code Generation

The service uses a collision-free approach:

1. Persist URL mapping and obtain a unique database-generated ID.
2. Convert the ID to a Base62 encoded string.

Example:

```text
1 -> b
2 -> c
61 -> 9
62 -> ba
```

Because database IDs are unique and Base62 encoding is deterministic, generated short codes cannot collide.

### Duplicate URLs

When the same URL is shortened multiple times, the service returns the existing short code instead of creating a new one.

Example:

```text
POST /shorten
https://google.com

=> abc123

POST /shorten
https://google.com

=> abc123
```

This behavior avoids duplicate records and makes the operation idempotent.

### Custom Aliases

Users may provide a custom alias:

```json
{
  "url": "https://google.com",
  "alias": "google"
}
```

If the alias already exists, the service returns:

```http
409 Conflict
```

---

## Technology Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- SQLite
- Gradle
- JUnit 5

---

## Prerequisites

Install:

- Java 21+
- Gradle 8+

Verify installation:

```bash
java -version
gradle -version
```

---

## Clone Repository

```bash
git clone <repository-url>
cd url-shortener
```

---

## Build Project

```bash
./gradlew clean build
```

---

## Run Application

```bash
./gradlew bootRun
```

Application starts on:

```text
http://localhost:8080
```

---

## API Documentation

### Create Short URL

**Request**

```http
POST /shorten
Content-Type: application/json
```

Body:

```json
{
  "url": "https://www.google.com"
}
```

Response:

```json
{
  "code": "b",
  "shortUrl": "http://localhost:8080/b"
}
```

---

### Create Short URL with Alias

Request:

```json
{
  "url": "https://www.google.com",
  "alias": "google"
}
```

Response:

```json
{
  "code": "google",
  "shortUrl": "http://localhost:8080/google"
}
```

---

### Redirect

Request:

```http
GET /google
```

Response:

```http
301 Moved Permanently
Location: https://www.google.com
```

---

### Unknown Code

Request:

```http
GET /unknown
```

Response:

```http
404 Not Found
```

---

## Example cURL Commands

### Shorten URL

```bash
curl --location 'http://localhost:8080/shorten' \
--header 'Content-Type: application/json' \
--data '{
    "url":"https://www.google.com"
}'
```

### Shorten URL with Alias

```bash
curl --location 'http://localhost:8080/shorten' \
--header 'Content-Type: application/json' \
--data '{
    "url":"https://www.google.com",
    "alias":"google"
}'
```

### Redirect

```bash
curl -v http://localhost:8080/google
```

---

## Run Tests

Execute all tests:

```bash
./gradlew test
```

Test reports are available under:

```text
build/reports/tests/test/index.html
```

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.aditya.urlshortener
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── generator
│   │       ├── repository
│   │       └── service
│   └── resources
│
└── test
    └── java
```

---

## Future Improvements

Given additional time, the following enhancements would be added:

- Redis caching for redirects
- URL expiration support
- Click analytics
- Rate limiting
- User accounts and ownership
- Distributed ID generation (Snowflake)
- Docker support
- OpenAPI / Swagger documentation
- Custom domain support
- QR code generation

---

## Assumptions

- Only HTTP and HTTPS URLs are accepted.
- Short codes are case-sensitive.
- Duplicate URLs return the same short code.
- Custom aliases must be unique.
- Redirects use HTTP 301 (Moved Permanently).