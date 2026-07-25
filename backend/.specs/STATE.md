# Project State

## Decisions

### AD-001 — JWT Authentication Strategy
- **Status:** active
- **Date:** 2026-07-25
- **Feature:** java25-migration
- **Decision:** JWT stateless com HMAC-SHA256 via `spring-boot-starter-oauth2-resource-server`. Endpoint de login: `POST /auth/login`. Claims: `sub=email` (email do usuário), `roles=[...]` (lista de role names). Chave simétrica via property `jwt.secret`.
- **Rationale:** Substitui o stack OAuth2 legado (`spring-security-oauth2-autoconfigure`) removido no Spring Boot 3. Sem Authorization Server externo. Padrão recomendado pelo Spring Security 6 para APIs standalone.

---

## Handoff

- **Feature**: `java25-migration` (`.specs/features/java25-migration/spec.md`)
- **Phase / Task**: Completed
- **Completed**: T1-T11 (All 11 tasks completed, 100% build & integration tests green)
- **In-progress**: none
- **Next step**: Run Verifier pass or trigger UAT / code review.
- **Blockers**: none
- **Uncommitted files**: none
- **Branch**: refactor/migracao-java-25

