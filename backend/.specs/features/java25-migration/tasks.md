# Java 25 Migration — Tasks

## Execution Protocol (MANDATORY -- do not skip)

Implement these tasks with the `tlc-spec-driven` skill: **activate it by name and follow its Execute flow and Critical Rules.** Do not search for skill files by filesystem path. The skill is the source of truth for the full flow (per-task cycle, sub-agent delegation, adequacy review, Verifier, discrimination sensor).

**If the skill cannot be activated, STOP and tell the user — do not proceed without it.**

---

**Design:** `.specs/features/java25-migration/design.md`
**Status:** Approved

---

## Test Coverage Matrix

> Generated from codebase sampling — confirmed before Execute. Guidelines found: none — strong defaults applied. Existing tests sampled: `GenreResourceIT.java`, `MovieResourceIT.java`, `ReviewResourceIT.java` (all integration tests via MockMvc + SpringBootTest). No unit tests found. The project tests exclusively at the integration layer (full Spring context, H2 in-memory).

| Code Layer | Required Test Type | Coverage Expectation | Location Pattern | Run Command |
|---|---|---|---|---|
| Controller / Route (novo AuthController) | integration | Happy path (200 + JWT), invalid credentials (401), malformed body (400) | `src/test/java/**/it/*IT.java` | `./mvnw test` |
| Service (JwtService) | integration | Via AuthController IT — token emitido e aceito pela resource server | `src/test/java/**/it/*IT.java` | `./mvnw test` |
| Config (SecurityConfig, AppConfig) | none | Build gate only — sem teste direto | — | `./mvnw clean package -DskipTests` |
| Entity / DTO (modificação de namespace) | none | Build gate only | — | `./mvnw clean package -DskipTests` |
| Existing ITs migrados | integration | Todos os testes existentes continuam passando; `obtainAccessToken()` usa novo endpoint | `src/test/java/**/it/*IT.java` | `./mvnw test` |

## Gate Check Commands

> Generated from pom.xml and existing Maven wrapper.

| Gate Level | When to Use | Command |
|---|---|---|
| Quick (build only) | Tasks de config/entity/DTO sem testes | `./mvnw clean package -DskipTests` |
| Full | Tasks com integration tests | `./mvnw test` |
| Build+Full | Após última task | `./mvnw clean package && ./mvnw test` |

---

## Execution Plan

```
Phase 1 → Phase 2 → Phase 3 → Phase 4

Phase 1: T1 → T2
Phase 2: T3 → T4 → T5 → T6
Phase 3: T7 → T8 → T9
Phase 4: T10 → T11
```

### Phase 1 — Foundation (pom + properties)

Remove dependências incompatíveis; estabelece o compilador Java 25.

```
T1 → T2
```

### Phase 2 — javax → jakarta + deletar stack OAuth2

Mecânica pura: renomear imports, deletar arquivos incompatíveis.

```
T3 → T4 → T5 → T6
```

### Phase 3 — Novo Stack de Autenticação

Criar os novos componentes de segurança e autenticação.

```
T7 → T8 → T9
```

### Phase 4 — Migração dos Testes + Verificação Final

Atualizar os ITs para o novo endpoint e fazer o build final passar.

```
T10 → T11
```

---

## Task Breakdown

### T1: Atualizar pom.xml (Boot 3.5.3, Java 25, remover dependências incompatíveis)

**What:** Substituir Boot parent de 2.4.4 para 3.5.3, `<java.version>` para 25, remover `spring-cloud-dependencies`, `spring-security-oauth2-autoconfigure`, `springfox-swagger2`, `springfox-swagger-ui`; adicionar `springdoc-openapi-starter-webmvc-ui:2.8.18` e `spring-boot-starter-oauth2-resource-server`.
**Where:** `pom.xml`
**Depends on:** None
**Reuses:** pom.xml existente
**Requirement:** MIG-03, MIG-04

**Done when:**
- [ ] `<parent><version>` é 3.5.3
- [ ] `<java.version>` é 25
- [ ] `<spring-cloud.version>` e `<dependencyManagement>` block removidos
- [ ] `spring-security-oauth2-autoconfigure` removido das dependencies
- [ ] `springfox-swagger2` e `springfox-swagger-ui` removidos
- [ ] `springdoc-openapi-starter-webmvc-ui:2.8.18` adicionado
- [ ] `spring-boot-starter-oauth2-resource-server` adicionado
- [ ] `./mvnw clean package -DskipTests` exits 0

**Tests:** none (config)
**Gate:** quick — `./mvnw clean package -DskipTests`
**Commit:** `chore(pom): migrate to Spring Boot 3.5.3 + Java 25, remove legacy OAuth2 and Springfox`

---

### T2: Atualizar application properties (remover oauth2 client properties)

**What:** Remover `security.oauth2.client.client-id` e `security.oauth2.client.client-secret` de `application-test.properties` e `application-dev.properties`. Atualizar comentários `javax.persistence` → `jakarta.persistence` em dev properties.
**Where:** `src/main/resources/application-test.properties`, `src/main/resources/application-dev.properties`
**Depends on:** T1
**Reuses:** Properties existentes (`jwt.secret`, `jwt.duration` permanecem)
**Requirement:** MIG-03

**Done when:**
- [ ] `security.oauth2.client.client-id` e `client-secret` removidos de ambos os arquivos
- [ ] `jwt.secret` e `jwt.duration` presentes em ambos os arquivos
- [ ] Comentários com `javax.persistence` atualizados para `jakarta.persistence` em dev.properties
- [ ] `./mvnw clean package -DskipTests` exits 0

**Tests:** none (config)
**Gate:** quick — `./mvnw clean package -DskipTests`
**Commit:** `chore(config): remove legacy OAuth2 client properties, keep jwt.secret + jwt.duration`

---

### T3: Migrar javax → jakarta em todas as entidades e LogFields

**What:** Substituir `import javax.persistence.*` por `import jakarta.persistence.*` em `User.java`, `Genre.java`, `Movie.java`, `Review.java`, `Role.java` e `LogFields.java`.
**Where:** `src/main/java/com/devgabriel/movieflix/entities/*.java`, `src/main/java/com/devgabriel/movieflix/common/LogFields.java`
**Depends on:** T1
**Reuses:** Código de entidade existente (só namespace)
**Requirement:** MIG-02

**Done when:**
- [ ] Zero ocorrências de `import javax.persistence` em `entities/` e `common/`
- [ ] `grep -r "import javax.persistence" src/main/` retorna vazio
- [ ] `./mvnw clean package -DskipTests` exits 0

**Tests:** none (entity — build gate only)
**Gate:** quick — `./mvnw clean package -DskipTests`
**Commit:** `refactor(entities): javax.persistence → jakarta.persistence`

---

### T4: Migrar javax → jakarta em DTOs e validators

**What:** Substituir `import javax.validation.*` por `import jakarta.validation.*` em todos os arquivos em `dtos/`.
**Where:** `src/main/java/com/devgabriel/movieflix/dtos/*.java`
**Depends on:** T1
**Reuses:** DTOs existentes (só namespace)
**Requirement:** MIG-02

**Done when:**
- [ ] Zero ocorrências de `import javax.validation` em `dtos/`
- [ ] `grep -r "import javax.validation" src/main/` retorna vazio
- [ ] `./mvnw clean package -DskipTests` exits 0

**Tests:** none (DTO — build gate only)
**Gate:** quick — `./mvnw clean package -DskipTests`
**Commit:** `refactor(dtos): javax.validation → jakarta.validation`

---

### T5: Deletar stack OAuth2 legado (AuthorizationServerConfig, ResourceServerConfig, WebSecurityConfig, JwtTokenEnhancer)

**What:** Deletar os 4 arquivos que dependem exclusivamente de `spring-security-oauth2-autoconfigure` e `WebSecurityConfigurerAdapter`, ambos removidos no Spring Boot 3.
**Where:**
- `src/main/java/com/devgabriel/movieflix/config/AuthorizationServerConfig.java` → DELETE
- `src/main/java/com/devgabriel/movieflix/config/ResourceServerConfig.java` → DELETE
- `src/main/java/com/devgabriel/movieflix/config/WebSecurityConfig.java` → DELETE
- `src/main/java/com/devgabriel/movieflix/components/JwtTokenEnhancer.java` → DELETE
**Depends on:** T1, T3, T4
**Requirement:** MIG-05, MIG-06, MIG-10

**Done when:**
- [ ] Os 4 arquivos não existem mais em `src/`
- [ ] `./mvnw clean package -DskipTests` exits 0 (sem erros de import ou bean faltando — os novos beans ainda não existem; o build pode falhar até T7; isso é aceitável — gate é apenas de compilação das classes remanescentes)

> **Nota:** O build pode falhar nesta task até que T7 (SecurityConfig) seja completada. Se o compilador reclamar de beans faltando, isso é esperado — o gate completo será verificado em T7.

**Tests:** none (deleção)
**Gate:** quick (parcial OK se falhar por beans faltando) — `./mvnw clean package -DskipTests`
**Commit:** `refactor(config): remove legacy OAuth2 stack (AuthorizationServer, ResourceServer, WebSecurity, JwtTokenEnhancer)`

---

### T6: Limpar AppConfig.java e deletar SwaggerConfig.java

**What:** Em `AppConfig.java`, remover os beans `JwtAccessTokenConverter` e `JwtTokenStore` (que dependem do stack OAuth2 deletado em T5); manter apenas o bean `BCryptPasswordEncoder`. Deletar `SwaggerConfig.java` (Springfox — incompatível com Boot 3; SpringDoc não precisa de config manual).
**Where:**
- `src/main/java/com/devgabriel/movieflix/config/AppConfig.java` → modificar
- `src/main/java/com/devgabriel/movieflix/config/SwaggerConfig.java` → DELETE
**Depends on:** T5
**Reuses:** Bean `BCryptPasswordEncoder` existente em AppConfig
**Requirement:** MIG-10

**Done when:**
- [ ] `AppConfig.java` contém apenas o bean `BCryptPasswordEncoder`
- [ ] `AppConfig.java` não importa `JwtAccessTokenConverter` nem `JwtTokenStore`
- [ ] `SwaggerConfig.java` não existe mais
- [ ] `./mvnw clean package -DskipTests` exits 0 (ainda pode ter erros de beans de segurança faltando — OK até T7)

**Tests:** none (config)
**Gate:** quick — `./mvnw clean package -DskipTests`
**Commit:** `refactor(config): strip AppConfig to BCryptPasswordEncoder only, delete SwaggerConfig`

---

### T7: Criar SecurityConfig.java (Spring Security 6 DSL)

**What:** Criar `SecurityConfig.java` que substitui `WebSecurityConfig` + `ResourceServerConfig` com a nova DSL do Spring Security 6: `SecurityFilterChain`, `AuthenticationManager` bean, regras de autorização por role, stateless session, `oauth2ResourceServer(jwt())`, CORS, H2 console frame-options para profile test.
**Where:** `src/main/java/com/devgabriel/movieflix/config/SecurityConfig.java` (novo)
**Depends on:** T6
**Reuses:** Regras de autorização de `ResourceServerConfig` (mesmos paths/roles); `UserService` como `UserDetailsService`; `BCryptPasswordEncoder` de `AppConfig`
**Requirement:** MIG-07

**Done when:**
- [ ] `@Bean SecurityFilterChain` configurado com: `/auth/login` e `/h2-console/**` públicos; `GET /genres/**`, `GET /movies/**` exigem VISITOR ou MEMBER; `POST /reviews/**` exige MEMBER ou ADMIN; demais requerem autenticação
- [ ] `@Bean AuthenticationManager` exposto para uso no `AuthController`
- [ ] Session management configurado como `STATELESS`
- [ ] `oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))` configurado
- [ ] CORS configurado (mesmas regras de `ResourceServerConfig`)
- [ ] Frame options disabled para profile `test` (H2 console)
- [ ] `./mvnw clean package -DskipTests` exits 0 (build limpo)

**Tests:** none (config — validado via ITs em T10)
**Gate:** quick — `./mvnw clean package -DskipTests`
**Commit:** `feat(security): add SecurityConfig with Spring Security 6 DSL, stateless JWT`

---

### T8: Criar JwtService.java (emite e valida JWT HMAC-SHA256)

**What:** Criar `JwtService.java` que: (1) expõe `@Bean JwtDecoder` usando `NimbusJwtDecoder.withSecretKey()` com chave HMAC-SHA256 derivada de `jwt.secret` — bean consumido automaticamente pelo `oauth2ResourceServer`; (2) fornece método `generateToken(Authentication auth)` que emite JWT com `sub=email`, `roles=[...]`, `iat=now`, `exp=now+jwtDuration`; (3) fornece `@Bean JwtAuthenticationConverter` que extrai roles do claim `roles` do JWT.
**Where:** `src/main/java/com/devgabriel/movieflix/services/JwtService.java` (novo)
**Depends on:** T7
**Reuses:** `jwt.secret` e `jwt.duration` (properties já existentes); Nimbus JOSE+JWT (transitivo via `spring-boot-starter-oauth2-resource-server`)
**Requirement:** MIG-08

**Done when:**
- [ ] `@Bean JwtDecoder` presente e usa `NimbusJwtDecoder.withSecretKey()`
- [ ] `generateToken()` emite JWT com claims `sub`, `roles`, `iat`, `exp`
- [ ] `@Bean JwtAuthenticationConverter` converte claim `roles` → `GrantedAuthority` com prefixo `ROLE_`
- [ ] `./mvnw clean package -DskipTests` exits 0

**Tests:** none direto (validado via AuthController IT em T9)
**Gate:** quick — `./mvnw clean package -DskipTests`
**Commit:** `feat(service): add JwtService with NimbusJwtDecoder and HMAC-SHA256 token generation`

---

### T9: Criar AuthController + DTOs + IT para /auth/login

**What:** Criar `LoginRequestDTO` (`{email, password}`), `TokenResponseDTO` (`{access_token, token_type, expires_in}`), e `AuthController` com `POST /auth/login` que usa `AuthenticationManager` + `JwtService`. Criar `AuthControllerIT.java` cobrindo: credenciais válidas (200 + JWT), credenciais inválidas (401), body malformado (400).
**Where:**
- `src/main/java/com/devgabriel/movieflix/dtos/LoginRequestDTO.java` (novo)
- `src/main/java/com/devgabriel/movieflix/dtos/TokenResponseDTO.java` (novo)
- `src/main/java/com/devgabriel/movieflix/resources/AuthController.java` (novo)
- `src/test/java/com/devgabriel/movieflix/tests/web/it/AuthControllerIT.java` (novo)
**Depends on:** T8
**Reuses:** Padrão de controller REST dos demais resources; `AuthenticationManager` de `SecurityConfig`; `JwtService` de T8
**Requirement:** MIG-09, MIG-01 (AC 1, 2, 4, 7, 8)

**Done when:**
- [ ] `POST /auth/login` com `{"email":"bob@gmail.com","password":"123456"}` retorna HTTP 200 com `{"access_token":"...","token_type":"Bearer","expires_in":86400}`
- [ ] `POST /auth/login` com senha errada retorna HTTP 401
- [ ] `POST /auth/login` com JSON malformado retorna HTTP 400
- [ ] JWT decodificado contém `sub=email` e `roles=[...]`
- [ ] `AuthControllerIT` com 3 testes passa
- [ ] `./mvnw test` exits 0 (apenas AuthControllerIT; outros ITs ainda falham — OK até T10)

**Tests:** integration
**Gate:** full — `./mvnw test -pl . -Dtest=AuthControllerIT` (ou `./mvnw test` se os demais ITs não causarem falha de compilação)
**Commit:** `feat(auth): add AuthController POST /auth/login with LoginRequestDTO and TokenResponseDTO + IT`

---

### T10: Migrar ITs existentes para novo endpoint /auth/login

**What:** Nos 3 ITs existentes (`GenreResourceIT`, `MovieResourceIT`, `ReviewResourceIT`): (1) remover `@Value("${security.oauth2.client.client-id/secret}")` fields e params; (2) substituir `obtainAccessToken()` para chamar `POST /auth/login` com JSON body em vez de `POST /oauth/token` com form params + httpBasic; (3) remover imports de `httpBasic` e `LinkedMultiValueMap`/`MultiValueMap` onde não mais necessários.
**Where:**
- `src/test/java/com/devgabriel/movieflix/tests/web/it/GenreResourceIT.java`
- `src/test/java/com/devgabriel/movieflix/tests/web/it/MovieResourceIT.java`
- `src/test/java/com/devgabriel/movieflix/tests/web/it/ReviewResourceIT.java`
**Depends on:** T9
**Reuses:** Estrutura de testes existente (apenas `obtainAccessToken()` muda)
**Requirement:** MIG-12

**Done when:**
- [ ] Nenhum dos 3 ITs referencia `security.oauth2.client.*` properties
- [ ] `obtainAccessToken()` usa `POST /auth/login` com `MediaType.APPLICATION_JSON` e JSON body
- [ ] `./mvnw test` com os 3 ITs passa (todos os testes existentes presentes e verdes)
- [ ] Contagem de testes: GenreResourceIT=3, MovieResourceIT=N, ReviewResourceIT=4 — nenhum removido

**Tests:** integration
**Gate:** full — `./mvnw test`
**Commit:** `test(it): migrate obtainAccessToken() from /oauth/token to /auth/login in all ITs`

---

### T11: Verificação final — build limpo + todos os testes + smoke do Swagger

**What:** Rodar o build completo e toda a suite de testes para confirmar que MIG-01 (build), MIG-02 (javax→jakarta), MIG-04 (Swagger) e MIG-12 (testes) estão satisfeitos. Verificar que `/v3/api-docs` responde (SpringDoc auto-configurado).
**Where:** Sem alteração de código — apenas verificação
**Depends on:** T10
**Requirement:** MIG-01, MIG-02, MIG-04, MIG-12

**Done when:**
- [ ] `./mvnw clean package` exits 0 (com testes)
- [ ] `./mvnw test` reports 0 failures, 0 errors
- [ ] `grep -r "import javax.persistence\|import javax.validation\|import javax.servlet" src/main/` retorna vazio
- [ ] App sobe com `./mvnw spring-boot:run` sem exceção no startup
- [ ] `curl -o /dev/null -w "%{http_code}" http://localhost:8080/v3/api-docs` retorna 200

**Tests:** integration (já rodados em T10)
**Gate:** build+full — `./mvnw clean package && ./mvnw test`
**Commit:** `chore(migration): java25 migration complete — Boot 3.5.3, Java 25, stateless JWT, SpringDoc`

---

## Phase Execution Map

```
Phase 1 → Phase 2 → Phase 3 → Phase 4

Phase 1: T1 ──→ T2
Phase 2: T3 ──→ T4 ──→ T5 ──→ T6
Phase 3: T7 ──→ T8 ──→ T9
Phase 4: T10 ──→ T11

Total: 11 tasks
```

---

## Task Granularity Check

| Task | Escopo | Status |
|---|---|---|
| T1: pom.xml | 1 arquivo, 1 conceito (dependências) | ✅ Granular |
| T2: properties | 2 arquivos config relacionados, 1 mudança (remover oauth2 props) | ✅ Granular |
| T3: javax→jakarta entities | 6 arquivos, 1 tipo de mudança (namespace) | ✅ Granular |
| T4: javax→jakarta DTOs | N arquivos, 1 tipo de mudança (namespace) | ✅ Granular |
| T5: deletar 4 arquivos OAuth2 | 4 deletes, 1 conceito (remover stack legado) | ✅ Granular |
| T6: limpar AppConfig + deletar SwaggerConfig | 1 modificação + 1 deleção, 1 conceito (finalizar limpeza config) | ✅ Granular |
| T7: SecurityConfig | 1 arquivo novo, 1 componente | ✅ Granular |
| T8: JwtService | 1 arquivo novo, 1 serviço | ✅ Granular |
| T9: AuthController + DTOs + IT | 4 arquivos — DTOs são suporte direto ao controller (não testáveis independentemente) | ✅ Granular (merge legítimo: DTOs só existem para o controller) |
| T10: migrar 3 ITs | 3 arquivos, 1 tipo de mudança (obtainAccessToken) | ✅ Granular |
| T11: verificação final | sem código — gate | ✅ Granular |

---

## Diagram-Definition Cross-Check

| Task | Depends On (task body) | Diagram mostra | Status |
|---|---|---|---|
| T1 | None | início Phase 1 | ✅ Match |
| T2 | T1 | T1 → T2 | ✅ Match |
| T3 | T1 | T2 → T3 (Phase 2 depende Phase 1) | ✅ Match |
| T4 | T1 | T3 → T4 | ✅ Match |
| T5 | T1, T3, T4 | T4 → T5 (e implícito T1 via Phase 1) | ✅ Match |
| T6 | T5 | T5 → T6 | ✅ Match |
| T7 | T6 | T6 → T7 (Phase 3 depende Phase 2) | ✅ Match |
| T8 | T7 | T7 → T8 | ✅ Match |
| T9 | T8 | T8 → T9 | ✅ Match |
| T10 | T9 | T9 → T10 (Phase 4 depende Phase 3) | ✅ Match |
| T11 | T10 | T10 → T11 | ✅ Match |

Nenhuma dependência aponta para task em fase posterior. ✅

---

## Test Co-location Validation

| Task | Camada criada/modificada | Matrix exige | Task diz | Status |
|---|---|---|---|---|
| T1 | config (pom.xml) | none | none | ✅ OK |
| T2 | config (properties) | none | none | ✅ OK |
| T3 | entity (namespace) | none | none | ✅ OK |
| T4 | DTO (namespace) | none | none | ✅ OK |
| T5 | deleção de config | none | none | ✅ OK |
| T6 | config (AppConfig, SwaggerConfig) | none | none | ✅ OK |
| T7 | config (SecurityConfig) | none (validado via ITs) | none | ✅ OK |
| T8 | service (JwtService) | integration (via controller IT) | none direto — validado via T9 IT | ✅ OK (merge forward em T9) |
| T9 | controller (AuthController) + DTOs | integration | integration — AuthControllerIT incluído | ✅ OK |
| T10 | ITs existentes (migração) | integration | integration | ✅ OK |
| T11 | sem código | — | integration (gate) | ✅ OK |

Nenhuma violação. ✅
