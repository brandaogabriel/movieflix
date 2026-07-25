# Java 25 Migration Specification

**Feature:** `java25-migration`
**Status:** Awaiting confirmation
**Created:** 2026-07-25

---

## Problem Statement

The Movieflix backend runs on Java 11 and Spring Boot 2.4.4 with an OAuth2 stack
(`spring-security-oauth2-autoconfigure`, Spring Cloud Hoxton) that was removed from
Spring Boot 3. The application cannot be built or run on Java 25 without a full
platform upgrade. The goal is to migrate to Java 25 + Spring Boot 3.5.x with minimum
code rewrite: only the changes required to compile, all tests passing, and functional
parity preserved.

---

## Goals

- [ ] Application compiles and starts on Java 25 + Spring Boot 3.5.x
- [ ] All existing tests pass after migration
- [ ] Functional parity: same 5 endpoints, same roles, same JWT behavior
- [ ] Authentication replaces the removed OAuth2 password-flow with stateless JWT via Spring Security 6 + `oauth2-resource-server`
- [ ] SpringDoc OpenAPI 3 replaces Springfox (Swagger UI remains available)
- [ ] Spring Cloud Hoxton dependency removed entirely
- [ ] `javax.*` namespace replaced by `jakarta.*` throughout

---

## Out of Scope

| Feature | Reason |
|---------|--------|
| Java 25 language features (Records, Sealed classes, etc.) | Conservadora: compilar e funcionar only |
| Spring Authorization Server | User chose stateless JWT without AS |
| New endpoints or business logic | This is a platform migration, not a feature sprint |
| CI/CD pipeline changes | Deployment changes are post-migration |
| Heroku deployment config | system.properties update is incidental; deploy guide unchanged |

---

## Assumptions & Open Questions

| Assumption / decision | Chosen default | Rationale | Confirmed? |
|-----------------------|----------------|-----------|-----------|
| Spring Boot target version | 3.5.3 | Latest stable; certified compatible with Java 25 (Boot 3.5 docs: requires Java 17+, supports up to Java 25) | y |
| JWT auth pattern | `spring-boot-starter-oauth2-resource-server` validates incoming tokens; new `POST /auth/login` issues JWT using Nimbus JOSE+JWT (included transitively) | Avoids full custom filter chain while removing Authorization Server; matches recommended Spring Security 6 pattern | y |
| Login endpoint path | `POST /auth/login` (replaces `POST /oauth/token`) | Clean break from OAuth2 nomenclature; client-facing contract change | y |
| Token payload | `sub` = user email, `roles` claim = list of role names, `exp` = now + jwt.duration seconds | Mirrors existing JwtTokenEnhancer behavior | y |
| Token validation key | Symmetric HMAC-SHA256 via `jwt.secret` property (new) | Simple for single-service; no external JWKS endpoint needed | y |
| SpringDoc version | 2.8.18 | Latest stable compatible with Spring Boot 3.0-3.5, Java 17+ | y |
| Springfox @Api* annotations | Removed (not ported) | Conservative scope: SpringDoc auto-discovers endpoints without annotations | y |
| H2 console path | `/h2-console/**` remains public in test profile | Preserve dev/test convenience | y |
| Spring Cloud | Removed entirely | Hoxton only provided BOM for the removed oauth2-autoconfigure; no other Cloud feature used | y |

**Open questions:** none — all resolved or logged above.

---

## User Stories

### P1: Build & Start on Java 25 ⭐ MVP

**User Story:** As a developer, I want the application to compile and start on Java 25 + Spring Boot 3.5 so that we run on a supported platform.

**Why P1:** Without this nothing else works.

**Acceptance Criteria:**

1. WHEN `./mvnw clean package -DskipTests` is run on Java 25 THEN the build SHALL succeed with exit code 0
2. WHEN `./mvnw spring-boot:run` is run with `test` profile THEN the application SHALL start and log `Started MovieflixApplication`
3. WHEN the application starts THEN there SHALL be no `ClassNotFoundException` or `NoSuchMethodError` at boot time

**Independent Test:** Run `./mvnw clean package -DskipTests` — must exit 0.

---

### P1: javax → jakarta Namespace Migration ⭐ MVP

**User Story:** As a developer, I want all `javax.*` imports replaced by `jakarta.*` so that the code is compatible with Jakarta EE 9+ (required by Spring Boot 3).

**Why P1:** Spring Boot 3 uses Jakarta EE 9; any `javax.*` import on a renamed package causes `ClassNotFoundException` at runtime.

**Acceptance Criteria:**

1. WHEN any Java source file in `src/` is scanned for `import javax.` THEN zero occurrences SHALL be found for packages renamed in Jakarta EE 9 (`javax.persistence`, `javax.validation`, `javax.servlet`, `javax.transaction`)
2. WHEN `./mvnw test` is run THEN all tests SHALL pass with no `ClassNotFoundException` related to javax

**Independent Test:** `grep -r "import javax\." src/` returns no hits on renamed packages.

---

### P1: Stateless JWT Authentication (replaces OAuth2 Password Flow) ⭐ MVP

**User Story:** As a client application, I want to authenticate via `POST /auth/login` with `{email, password}` and receive a JWT so that I can call protected endpoints.

**Why P1:** The entire existing security model depended on `spring-security-oauth2-autoconfigure` which does not exist in Spring Boot 3. Without a replacement, all endpoints return 401.

**Acceptance Criteria:**

1. WHEN `POST /auth/login` is called with valid `{"email": "...", "password": "..."}` THEN the system SHALL return HTTP 200 with `{"access_token": "<jwt>", "token_type": "Bearer", "expires_in": <seconds>}`
2. WHEN `POST /auth/login` is called with invalid credentials THEN the system SHALL return HTTP 401
3. WHEN a protected endpoint is called with a valid Bearer token THEN the system SHALL return the appropriate business response (2xx)
4. WHEN a protected endpoint is called without a token THEN the system SHALL return HTTP 401
5. WHEN a `VISITOR` role token is used on `POST /reviews` THEN the system SHALL return HTTP 403
6. WHEN a `MEMBER` role token is used on `POST /reviews` THEN the system SHALL return HTTP 201
7. WHEN the JWT is decoded THEN it SHALL contain `sub` (user email) and `roles` claims
8. WHEN a JWT with an invalid signature is used THEN the system SHALL return HTTP 401

**Independent Test:** cURL sequence: login → get token → call `GET /genres` with token → call `POST /reviews` with VISITOR token (expect 403) → call `POST /reviews` with MEMBER token (expect 201).

---

### P1: SpringDoc OpenAPI 3 (replaces Springfox) ⭐ MVP

**User Story:** As a developer, I want the Swagger UI available at `/swagger-ui.html` so that I can explore the API interactively.

**Why P1:** Springfox 2.9.2 throws `NullPointerException` on Spring Boot 3 startup, preventing the application from starting at all.

**Acceptance Criteria:**

1. WHEN the application starts THEN there SHALL be no exception from Springfox or SpringDoc at boot time
2. WHEN `GET /swagger-ui.html` is called THEN the system SHALL return HTTP 200 with the Swagger UI page
3. WHEN `GET /v3/api-docs` is called THEN the system SHALL return HTTP 200 with a valid OpenAPI 3 JSON document

**Independent Test:** Start app, `curl -o /dev/null -w "%{http_code}" http://localhost:8080/v3/api-docs` returns 200.

---

### P1: All Existing Tests Pass ⭐ MVP

**User Story:** As a developer, I want all existing tests to pass after the migration so that I have confidence functional parity is preserved.

**Why P1:** Tests are the regression safety net; if they don't pass, the migration is incomplete.

**Acceptance Criteria:**

1. WHEN `./mvnw test` is run THEN the test suite SHALL report 0 failures and 0 errors
2. WHEN `./mvnw test` is run THEN all tests that existed pre-migration SHALL still be present and executed

**Independent Test:** `./mvnw test` — green.

---

## Edge Cases

- WHEN `/h2-console/**` is accessed in `test` profile THEN system SHALL allow access (frame options disabled)
- WHEN a JWT is tampered (signature invalid) THEN system SHALL return 401
- WHEN `POST /auth/login` receives malformed JSON THEN system SHALL return 400
- WHEN a JWT has no `roles` claim THEN system SHALL treat user as unauthenticated for role-gated endpoints

---

## Requirement Traceability

| Requirement ID | Story | Phase | Status |
|----------------|-------|-------|--------|
| MIG-01 | P1: Build & Start | Tasks | Pending |
| MIG-02 | P1: javax → jakarta | Tasks | Pending |
| MIG-03 | P1: pom.xml — Boot 3.5 + Java 25 + remove Cloud | Tasks | Pending |
| MIG-04 | P1: Remove Springfox, add SpringDoc 2.8.18 | Tasks | Pending |
| MIG-05 | P1: Remove AuthorizationServerConfig | Tasks | Pending |
| MIG-06 | P1: Remove WebSecurityConfig (legacy) | Tasks | Pending |
| MIG-07 | P1: New SecurityConfig (Spring Security 6 DSL) | Tasks | Pending |
| MIG-08 | P1: New JwtService (sign + validate HMAC-SHA256) | Tasks | Pending |
| MIG-09 | P1: New AuthController POST /auth/login | Tasks | Pending |
| MIG-10 | P1: Remove/update JwtTokenEnhancer + AppConfig | Tasks | Pending |
| MIG-11 | P1: Remove ResourceServerConfig → merge into SecurityConfig | Tasks | Pending |
| MIG-12 | P1: All tests pass (update test security setup) | Tasks | Pending |

**Coverage:** 12 total, 12 mapped to tasks, 0 unmapped ✅

---

## Success Criteria

- [ ] `./mvnw clean package` exits 0 on Java 25
- [ ] `./mvnw test` reports 0 failures
- [ ] `POST /auth/login` issues a valid JWT
- [ ] All 5 endpoints respond correctly with roles enforced
- [ ] `GET /swagger-ui.html` returns 200
- [ ] Zero `javax.*` imports on renamed packages remain in `src/`
