# URL Shortener – Design & AI Usage Write-Up

## 1. What did you ask the AI to do, and what did you write or decide yourself?

I used AI primarily as a productivity tool to accelerate implementation and reduce boilerplate. Specifically, I asked it to:

- Generate an initial Spring Boot project structure.
- Create DTOs, repository interfaces, exception classes, and controller skeletons.
- Suggest test cases for core functionality and edge cases.
- Generate README and API documentation templates.

The key architectural and behavioral decisions were made by me, including:

- Choosing Java 21 + Spring Boot + SQLite.
- Designing the short-code generation strategy using database-generated IDs and Base62 encoding.
- Deciding that shortening the same URL multiple times should return the existing short code rather than generating a new one.
- Defining custom alias behavior and returning HTTP 409 when an alias already exists.
- Defining validation rules to accept only HTTP and HTTPS URLs.
- Organizing the code into controller, service, repository, and persistence layers.

I reviewed all generated code and modified it where necessary to ensure I understood and could explain every part of the implementation.

---

## 2. Where did you override, correct, or throw away the AI’s output — and why?

One of the most significant corrections was around short-code generation.

The AI initially suggested generating random strings using UUIDs or random number generators. While these approaches can work, they either require collision detection or rely on probabilistic uniqueness. I replaced this with a deterministic approach:

- Persist the record.
- Use the database-generated unique ID.
- Convert the ID into a Base62 encoded string.

This guarantees uniqueness without collision checks and keeps the implementation simple.

I also rejected suggestions to introduce additional infrastructure such as Redis, Docker, distributed ID generators, or analytics tracking. While useful in production, they added complexity without contributing to the core requirements of the exercise.

Finally, I simplified several generated classes to reduce unnecessary abstractions and keep the codebase small enough to explain comfortably during a follow-up discussion.

---

## 3. The two or three biggest trade-offs I made, and the alternatives I considered

### Trade-off 1: Deterministic IDs vs Random Code Generation

I chose database IDs encoded as Base62.

**Alternative considered:**
Random alphanumeric strings.

**Reason for rejection:**
Random generation requires collision detection and retries. Using database IDs guarantees uniqueness and makes the behavior easy to reason about.

---

### Trade-off 2: Idempotent URL Shortening vs New Code per Request

I chose to return the same short code when the same URL is shortened multiple times.

**Alternative considered:**
Generate a new short code every time.

**Reason for rejection:**
Returning an existing code avoids duplicate records, reduces storage requirements, and makes the operation idempotent. It also simplifies future analytics because all traffic for a URL is associated with a single code.

---

### Trade-off 3: SQLite vs External Database

I chose SQLite as the datastore.

**Alternative considered:**
PostgreSQL.

**Reason for rejection:**
SQLite satisfies the persistence requirement while keeping setup and execution simple for reviewers. The application can be cloned and run without requiring external infrastructure.

---

## 4. What’s missing, or what would I do with another day?

With additional time, I would focus on production-oriented capabilities rather than adding more endpoints.

Areas I would prioritize include:

- Redis caching to reduce database reads during redirects.
- Click analytics and visit tracking.
- URL expiration and automatic cleanup.
- Rate limiting to prevent abuse.
- Docker packaging and containerized deployment.
- OpenAPI/Swagger documentation.
- Improved alias validation rules and reserved keyword handling.
- Database migrations using Flyway or Liquibase.
- Better observability through metrics and structured logging.

For large-scale deployments, I would also replace database-generated IDs with a distributed ID generation strategy (for example, Snowflake IDs) to support multiple application instances without relying on a single database for code generation.

Overall, I optimized for correctness, simplicity, and maintainability while ensuring the implementation could be easily extended in a follow-up session.