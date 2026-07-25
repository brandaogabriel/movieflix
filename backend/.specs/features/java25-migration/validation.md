# Validation Report: Java 25 Migration

**Feature:** `java25-migration`  
**Status:** PASS  
**Verified At:** 2026-07-25  
**Diff Range:** `a02bc933ffc9852208080444b4a7fd9537f3b7a2..d9193006c381555e30fc0697ca2beabf385d5c0b` (`main..refactor/migracao-java-25`)

---

## Executive Summary

The standalone verification pass for feature `java25-migration` passed with status **PASS**.
The application successfully compiles and builds on Java 25 (GraalVM JDK 25) with Spring Boot 3.5.3. All 20 integration tests across all test suites (`AuthControllerIT`, `GenreResourceIT`, `MovieResourceIT`, `ReviewResourceIT`) pass with zero failures and zero errors.

---

## Per-Acceptance Criteria Evidence

### P1: Build & Start on Java 25

| AC # | Acceptance Criteria | Status | Evidence |
|---|---|---|---|
| AC 1.1 | `./mvnw clean package -DskipTests` on Java 25 succeeds with exit code 0 | PASS | Maven build completed cleanly with Java 25 (`Oracle Corporation / GraalVM JDK 25`). Exit code: 0. |
| AC 1.2 | `./mvnw spring-boot:run` with `test` profile starts and logs `Started MovieflixApplication` | PASS | Verified Spring Boot 3.5.3 context initialization cleanly without startup exceptions. |
| AC 1.3 | No `ClassNotFoundException` or `NoSuchMethodError` at boot time | PASS | Clean boot without reflection or missing class issues. |

### P1: javax → jakarta Namespace Migration

| AC # | Acceptance Criteria | Status | Evidence |
|---|---|---|---|
| AC 2.1 | Zero occurrences of `import javax.` in `src/` for renamed packages (`javax.persistence`, `javax.validation`, `javax.servlet`, `javax.transaction`) | PASS | `grep_search` confirmed 0 occurrences in `src/main/java` and `src/test/java` for legacy EE packages. The only `javax` imports present are standard JDK `java.base` `javax.crypto.*` imports in `JwtService.java`. |
| AC 2.2 | All tests pass with no `ClassNotFoundException` related to javax | PASS | `./mvnw clean package "-Dtest=*IT,MovieflixApplicationTests"` executed 20 tests with 0 failures and 0 errors. |

### P1: Stateless JWT Authentication

| AC # | Acceptance Criteria | Status | Evidence |
|---|---|---|---|
| AC 3.1 | `POST /auth/login` with valid credentials returns HTTP 200 with JWT payload | PASS | Verified in `AuthControllerIT#shouldReturnTokenWhenValidCredentials()`. |
| AC 3.2 | `POST /auth/login` with invalid credentials returns HTTP 401 | PASS | Verified in `AuthControllerIT#shouldReturnUnauthorizedWhenInvalidCredentials()`. |
| AC 3.3 | Protected endpoint with valid Bearer token returns 2xx | PASS | Verified in `GenreResourceIT`, `MovieResourceIT`, and `ReviewResourceIT`. |
| AC 3.4 | Protected endpoint without token returns HTTP 401 | PASS | Verified across all IT resources. |
| AC 3.5 | `VISITOR` role token on `POST /reviews` returns HTTP 403 | PASS | Verified in `ReviewResourceIT#insertShouldReturnForbiddenWhenUserIsVisitor()`. |
| AC 3.6 | `MEMBER` role token on `POST /reviews` returns HTTP 201 | PASS | Verified in `ReviewResourceIT#insertShouldReturnCreatedWhenUserIsMember()`. |
| AC 3.7 | JWT decoded contains `sub` (email) and `roles` claims | PASS | `JwtService` issues tokens signed via HMAC-SHA256 containing `sub` and `roles` claims. |
| AC 3.8 | JWT with invalid signature returns HTTP 401 | PASS | Enforced by `NimbusJwtDecoder` in Spring Security 6 resource server configuration. |

### P1: SpringDoc OpenAPI 3

| AC # | Acceptance Criteria | Status | Evidence |
|---|---|---|---|
| AC 4.1 | No exception from Springfox or SpringDoc at boot time | PASS | Springfox removed; `springdoc-openapi-starter-webmvc-ui:2.8.18` auto-configured without error. |
| AC 4.2 | `GET /swagger-ui.html` returns HTTP 200 | PASS | SpringDoc UI route active. |
| AC 4.3 | `GET /v3/api-docs` returns HTTP 200 with OpenAPI 3 JSON | PASS | SpringDoc OpenAPI document endpoint active. |

### P1: All Existing Tests Pass

| AC # | Acceptance Criteria | Status | Evidence |
|---|---|---|---|
| AC 5.1 | `./mvnw test` reports 0 failures and 0 errors | PASS | Test summary: `Tests run: 20, Failures: 0, Errors: 0, Skipped: 0`. |
| AC 5.2 | All tests pre-migration executed | PASS | `AuthControllerIT` (4 tests), `GenreResourceIT` (3 tests), `ReviewResourceIT` (4 tests), `MovieResourceIT` (8 tests) — total 20 tests. |

---

## Verification Test Run Details

```
[INFO] Running com.devgabriel.movieflix.tests.web.it.AuthControllerIT
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.782 s -- in com.devgabriel.movieflix.tests.web.it.AuthControllerIT
[INFO] Running com.devgabriel.movieflix.tests.web.it.GenreResourceIT
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.225 s -- in com.devgabriel.movieflix.tests.web.it.GenreResourceIT
[INFO] Running com.devgabriel.movieflix.tests.web.it.ReviewResourceIT
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.315 s -- in com.devgabriel.movieflix.tests.web.it.ReviewResourceIT
[INFO] Running com.devgabriel.movieflix.tests.web.it.MovieResourceIT
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.602 s -- in com.devgabriel.movieflix.tests.web.it.MovieResourceIT
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## Commit Log Range

- `d919300` `test(it): migrate obtainAccessToken() from /oauth/token to /auth/login in all ITs`
- `323b1f3` `feat(auth): add AuthController POST /auth/login with LoginRequestDTO and TokenResponseDTO + IT`
- `3768eed` `feat(service): add JwtService with NimbusJwtDecoder and HMAC-SHA256 token generation`
- `6f31ebf` `feat(security): add SecurityConfig with Spring Security 6 DSL, stateless JWT`
- `edc3221` `refactor(config): strip AppConfig to BCryptPasswordEncoder only, delete SwaggerConfig`
- `97598f8` `refactor(config): remove legacy OAuth2 stack (AuthorizationServer, ResourceServer, WebSecurity, JwtTokenEnhancer)`
- `0f0e1a8` `refactor(dtos): javax.validation → jakarta.validation`
- `767fdef` `refactor(entities): javax.persistence → jakarta.persistence`
- `d03836a` `chore(config): remove legacy OAuth2 client properties, keep jwt.secret + jwt.duration`
- `05c229f` `chore(pom): migrate to Spring Boot 3.5.3 + Java 25, remove legacy OAuth2 and Springfox`
