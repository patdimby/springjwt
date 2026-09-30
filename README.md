# SpringJWT Expense Tracker

A server-rendered expense-management starter built with Spring Boot, Thymeleaf, JPA and MySQL. It combines browser session login with a separate JWT-based email verification workflow.

**Java 17 · Spring Boot 3.5.0 · Thymeleaf · Spring Security · MySQL · BCrypt · JJWT · JUnit 5 · Mockito · H2 · JaCoCo**

## Features

- Browser registration and username/password session login.
- BCrypt password hashing for both browser and JSON registration paths.
- Expense creation, listing, editing, deletion, keyword/date filtering and totals.
- Correct external expense identifiers preserved across DTO mapping and edits.
- JSON signup with SMTP verification email and signed one-day JWT links.
- Protected expense pages, CSRF-protected browser mutations, public static resources.
- Unit and full-context web tests with isolated H2 storage and mocked email delivery.
- Maven Wrapper and GitHub Actions verification.

Despite the repository name, JWT is used for **email verification**, not bearer authentication for expense routes. Browser login uses a session. Expenses are a shared collection visible to all authenticated users; no per-user ownership is implemented.

## Quick start

Use Java 17 and MySQL for a normal application run. Maven is provided by the wrapper.

Create an empty `simplejwt` database and a dedicated account. Set environment variables from the repository root:

```bash
export DB_URL='jdbc:mysql://localhost:3306/simplejwt'
export DB_USERNAME='your_database_user'
export DB_PASSWORD='your_database_password'
export SPRING_PROFILES_ACTIVE=local
bash ./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
$env:DB_URL='jdbc:mysql://localhost:3306/simplejwt'
$env:DB_USERNAME='your_database_user'
$env:DB_PASSWORD='your_database_password'
$env:SPRING_PROFILES_ACTIVE='local'
.\mvnw.cmd spring-boot:run
```

Open **http://localhost:8080/req/register**, create an account, then sign in at **http://localhost:8080/req/login** using its username. Successful login opens **/req/expenses**. No sample accounts or expenses are seeded.

The `local` profile enables Hibernate `update` for development. The default profile uses `validate` and requires an existing schema. Add versioned migrations before production use. Spring reads environment variables, not `.env` files automatically.

## Configuration

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/simplejwt` | MySQL connection |
| `DB_USERNAME` | `root` | Database user; override for deployment |
| `DB_PASSWORD` | Empty | Database password |
| `DDL_AUTO` | `validate` outside local profile | Schema policy |
| `SMTP_HOST`, `SMTP_PORT` | `localhost`, `1025` | SMTP server |
| `SMTP_USERNAME` | `no-reply@example.com` | Sender address / authentication username |
| `SMTP_PASSWORD` | Empty | SMTP password |
| `SMTP_AUTH`, `SMTP_STARTTLS` | `false` | SMTP authentication/TLS options |
| `SERVER_PORT` | `8080` | HTTP port |

Normal browser registration does not send email. JSON signup requires a usable SMTP server to deliver verification links. The mail helper currently catches delivery exceptions; a successful signup response does not prove email delivery.

The original committed database credential has been removed. Rotate it at its source if it was used beyond local development.

## Routes

| Method | Route | Behavior |
| --- | --- | --- |
| GET / POST | `/req/register` | Browser registration |
| GET | `/req/login` | Login page |
| POST | `/login` | Spring Security username/password processing |
| POST | `/logout` | Session logout with CSRF protection |
| POST | `/req/signup` | Public JSON signup / verification-email resend |
| GET | `/req/signup/verify?token=...` | Consume verification link |
| GET | `/req/expenses` | Shared expense list and total |
| GET | `/req/createExpense` | New expense form |
| POST | `/req/saveOrUpdateExpense` | Save form / update existing record |
| GET | `/req/updateExpense?id=...` | Edit by external expense UUID |
| POST | `/req/deleteExpense?id=...` | Delete expense, with CSRF |
| GET | `/req/filterExpenses` | Keyword/date filter and date/amount sorting |
| GET | `/req/index` | Redirect to expense list |

Expense routes require a logged-in session. Static resources are under `/req/css` and `/req/js`. Browser forms rendered by Thymeleaf include CSRF fields. Deletion is a POST operation, so the original GET deletion links are replaced with forms.

Dates use `dd/MM/yyyy`. Impossible calendar dates are rejected. Totals use `BigDecimal` and two-decimal numeric output; the view supplies its existing rupee symbol. Expense validation is still limited: it is not a complete accounting or currency-management system.

## Email verification

JSON signup accepts a user-shaped body such as:

```json
{"username":"ada","email":"ada@example.com","password":"example-password"}
```

The server hashes the password, resets caller-supplied identity/verification state, stores a verification token and calls the email helper. The first valid verification link sets `verified=true`, clears the stored token, and returns `201`; reused or invalid tokens return `403`.

The JWT signing key is generated once per JVM and is not externally configurable in this implementation. Restarting invalidates outstanding links; multiple instances do not share a signing key. No bearer-login token endpoint, refresh token or completed password-reset route exists.

Verification state is not currently enforced by session login. Browser registration and email-based JSON signup are distinct paths, and browser accounts can sign in before email verification. Reconcile these policies before relying on verification as an access gate.

## Tests and build

```bash
bash ./mvnw -B verify
# Windows: .\mvnw.cmd -B verify
```

**Verified:** 21 tests passed and an executable JAR was produced. JaCoCo line coverage is 73.0% (178/244 lines).

Reports are in `target/surefire-reports/` and `target/site/jacoco/index.html`. Run the JAR with the same configured database/profile:

```bash
java -jar target/springjwt-0.0.1.1.jar
```

Tests use H2 and mock `EmailService`; no real SMTP messages are sent. See [TESTING.md](TESTING.md) for scope. GitHub Actions runs the suite with Java 17 and publishes reports.

## Structure and compatibility

```text
controller/    Browser routes, JSON signup and email verification
service/       Expense operations, account lookup and mail composition
entity/        JPA account and expense data
repository/    Persistence and expense filter queries
dto/, mapper/  Form/view models and explicit field conversion
util/          Date formatting and verification JWT helpers
resources/     Thymeleaf pages and static scripts/styles
```

The original Spring Boot `4.0.0-SNAPSHOT` / Java 23 baseline was replaced with fixed Spring Boot `3.5.0` / Java 17 for reproducible compatibility with the existing APIs. This is a verified compatibility baseline, not a claim to be the latest release. Automatic Spring Data REST exposure was removed so entities and password hashes are not exposed through generated repository endpoints.

Read [ARCHITECTURE.md](ARCHITECTURE.md) and [GITHUB_DESCRIPTION.md](GITHUB_DESCRIPTION.md).

## Remaining work and license

Add a coherent verification/login policy, per-user expense ownership if needed, durable token keys, transactional mail delivery, validation/error handling, pagination and migrations before production deployment. The confirmation-password form field is not validated on the server. Unknown expense IDs and malformed date input are not yet consistently mapped to friendly error pages. Username uniqueness and duplicate browser-registration handling need further work.

No project license file was supplied; choose one before advertising reuse terms.
