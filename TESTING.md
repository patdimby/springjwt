# Testing — SpringJWT

Verified on 2026-09-30 with OpenJDK 17.0.20 and Maven Wrapper 3.9.10:

| Check | Result |
| --- | --- |
| Maven `verify` | BUILD SUCCESS |
| Tests | 21 passed, 0 failures, 0 errors |
| JaCoCo line coverage | 73.0% (178 covered / 244 lines) |
| Executable application JAR | Produced |

Suite: 9 expense/date/mapper/service unit tests, 3 JWT tests, 8 full-context web tests and 1 application-context test.

```bash
bash ./mvnw -B verify
```

Reports are in `target/surefire-reports/` and `target/site/jacoco/`. A portable report bundle is included as `verification-reports.zip` in the delivery.

Web tests use the real Spring context, security chain, MVC/Thymeleaf, JPA and H2. Only the email service is mocked. Tests verify registration hashing, username/password login, invalid fields, protected pages, public assets, email verification and replay rejection, expense persistence/edit/filter/delete and CSRF enforcement. No SMTP message is sent.

Unit tests verify external expense ID mapping, precise totals, valid/impossible dates, null-safe date validation and filter defaults, amount sorting, new UUIDs, missing expenses and JWT wrong-signature/malformed-token errors.

Tests do not cover live MySQL or SMTP, browser JavaScript interactions, load, deployment, per-user ownership (not implemented), all verification resend cases, or every mail-helper branch. Coverage has no enforced threshold. GitHub Actions has been added but not executed remotely. Spring's deprecated `MockBean` API is retained for the compatible test baseline.

Accompanying verified fixes remove duplicate login mappings, broken template fields/paths and redirects, plaintext browser-password storage, anonymous expense access, GET deletion, null filter crashes, lost external IDs and malformed-verification server errors. Configuration uses environment credentials. The Java 17 / Spring Boot 3.5.0 baseline replaces the original Java 23 / Boot 4 snapshot.
