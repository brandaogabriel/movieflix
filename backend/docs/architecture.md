# Movieflix Backend — Arquitetura

## Sumário Executivo

O Movieflix Backend é uma API REST Java construída com Spring Boot 3.5.3, seguindo uma **Arquitetura em Camadas (Layered Architecture)** clássica. A segurança é gerenciada por JWT stateless (Spring Security 6 via oauth2-resource-server), com controle de acesso baseado em roles (VISITOR e MEMBER).

---

## Padrão Arquitetural

### Layered Architecture (3 camadas)

```
┌──────────────────────────────────────────────────────┐
│              Cliente (HTTP / JWT)                     │
└──────────────────────────┬───────────────────────────┘
                           │ HTTP Request + Bearer Token
                           ▼
┌──────────────────────────────────────────────────────┐
│           Camada de Segurança (Security Filter)       │
│                SecurityConfig + JwtService            │
│           Valida JWT, verifica roles                  │
└──────────────────────────┬───────────────────────────┘
                           │ Request autorizada
                           ▼
┌──────────────────────────────────────────────────────┐
│             Camada de Recurso (Resources)             │
│     GenreResource | MovieResource | ReviewResource    │
│   @RestController — deserialização, HTTP mapping      │
└──────────────────────────┬───────────────────────────┘
                           │ DTOs
                           ▼
┌──────────────────────────────────────────────────────┐
│              Camada de Serviço (Services)             │
│  GenreService | MovieService | ReviewsService         │
│  UserService | AuthService                            │
│   @Service — lógica de negócio, @Transactional        │
│   Converte Entity ↔ DTO                              │
└──────────────────────────┬───────────────────────────┘
                           │ Entities
                           ▼
┌──────────────────────────────────────────────────────┐
│           Camada de Repositório (Repositories)        │
│  GenreRepository | MovieRepository | ReviewRepository │
│  UserRepository | RoleRepository                      │
│   Spring Data JPA (interfaces JpaRepository<T, ID>)  │
└──────────────────────────┬───────────────────────────┘
                           │ SQL/JPA
                           ▼
┌──────────────────────────────────────────────────────┐
│                  Banco de Dados                       │
│         H2 (test) / PostgreSQL (dev, prod)           │
└──────────────────────────────────────────────────────┘
```

---

## Padrões de Design Utilizados

| Padrão | Onde | Descrição |
|--------|------|-----------|
| **Layered Architecture** | Todo o projeto | Resource → Service → Repository |
| **DTO Pattern** | `dtos/` | Isola entidades de domínio da API HTTP |
| **Repository Pattern** | `repositories/` | Abstração de acesso a dados via Spring Data JPA |
| **Controller Advice** | `ResourceExceptionHandler` | Tratamento centralizado de exceções (`@ControllerAdvice`) |
| **Auditing (MappedSuperclass)** | `LogFields` | Campos de auditoria herdados por entidades |
| **Template Method** | Herança de `LogFields` | Comportamento comum nas entidades |

---

## Segurança — Autenticação Stateless JWT

### Fluxo de Autenticação

```
Cliente                                                  Resource Server
  │                                                             │
  │  POST /auth/login                                           │
  │  ({ email, password })                                      │
  │─────────────────────────────►                               │
  │                                                             │
  │                   Valida credenciais                        │
  │                   Gera JWT (HMAC-SHA256)                    │
  │                                                             │
  │◄─────────────────────────────                               │
  │  { access_token: JWT }                                      │
  │                              │                              │
  │  GET /movies                 │                              │
  │  Authorization: Bearer JWT   │                              │
  │─────────────────────────────────────────────────────────────►
  │                              │                              │
  │                              │              Valida JWT      │
  │                              │              Verifica role   │
  │◄─────────────────────────────────────────────────────────────
  │  200 OK + dados              │                              │
```

### Componentes de Segurança

| Componente | Responsabilidade |
|------------|-----------------|
| `SecurityConfig` | Configura o filtro de segurança, stateless session, roles por endpoint |
| `JwtService` | Assina e valida tokens JWT via HMAC-SHA256 usando `jwt.secret` |
| `AuthController` | Recebe login, valida credenciais via AuthenticationManager, retorna JWT |
| `UserService` | Implementa `UserDetailsService.loadUserByUsername(email)` |
| `AuthService` | `authenticated()` — recupera o `User` do `SecurityContextHolder` |

### Regras de Acesso

```java
// SecurityConfig
http.authorizeRequests()
    .antMatchers(HttpMethod.GET, "/genres/**").authenticated()
    .antMatchers(HttpMethod.GET, "/movies/**").authenticated()
    .antMatchers(HttpMethod.POST, "/reviews/**").hasRole("MEMBER")
```

---

## Módulos e Responsabilidades

### 1. `config/` — Configurações Spring

```
AppConfig.java                # Bean: BCryptPasswordEncoder
SecurityConfig.java           # Spring Security 6, regras HTTP e OAuth2 Resource Server
```

### 2. `entities/` — Domínio de Negócio

Todas as entidades herdam de `LogFields` (exceto `Role`) e implementam `equals/hashCode` baseados em `id`.

```
Genre → Movie (1:N)
Movie ← Review (N:1) — um movie tem muitas reviews
User ← Review (N:1) — um user escreve muitas reviews
User ↔ Role (N:M) — via tb_user_role
```

### 3. `dtos/` — Contratos da API

Os DTOs possuem construtores que recebem a entidade para conversão automática:

```java
// Exemplo: MovieDTO
public MovieDTO(Movie entity) {
    this.genreId = entity.getGenre().getId();  // "achata" o relacionamento
    this.reviews = entity.getReviews().stream()
        .map(ReviewDTO::new)
        .collect(Collectors.toList());
}
```

**Padrão de flattening:**
- `MovieDTO.genreId` ← `Movie.genre.id`
- `ReviewDTO.movieId` ← `Review.movie.id`
- `ReviewDTO.user` ← `UserDTO` completo (com roles, sem password)

### 4. `services/` — Regras de Negócio

| Serviço | Métodos | Transações |
|---------|---------|-----------|
| `GenreService` | `findAll()` | `readOnly = true` |
| `MovieService` | `findAll(Pageable)`, `findById(Long)` | `readOnly = true` |
| `ReviewsService` | `insert(ReviewDTO)` | `@Transactional` |
| `UserService` | `loadUserByUsername(String)` | `readOnly = true` |
| `AuthService` | `authenticated()` | — |

### 5. `resources/` — Controllers REST

| Controller | Endpoints | Retorno |
|------------|-----------|---------|
| `GenreResource` | `GET /genres` | `List<GenreDTO>` |
| `MovieResource` | `GET /movies`, `GET /movies/{id}` | `Page<MovieDTO>`, `MovieDTO` |
| `ReviewResource` | `POST /reviews` | `ReviewDTO` (HTTP 201) |

---

## Tratamento de Exceções

### Hierarquia de Exceções de Serviço

```
RuntimeException
    └── ResourceNotFoundException  → HTTP 404
    └── ForbiddenException         → HTTP 403
    └── UnauthorizedException      → HTTP 401
```

### `ResourceExceptionHandler` (@ControllerAdvice)

Captura todas as exceções e retorna:
- `StandardError` — para erros genéricos (404, 403, 401)
- `ValidationError` — extends `StandardError` + `List<FieldMessage>` (422)

---

## Configuração Multi-Ambiente

| Perfil | Datasource | DDL | Seed |
|--------|-----------|-----|------|
| `test` (padrão) | H2 in-memory | auto-create | `data.sql` executado |
| `dev` | PostgreSQL local (5432/movieflix) | validate/update | manual |
| `prod` | PostgreSQL Heroku (DATABASE_URL) | validate | manual |

**Ativação:**
```bash
# test (padrão — APP_PROFILE não definido)
mvn spring-boot:run

# dev
APP_PROFILE=dev mvn spring-boot:run

# prod (Heroku define automaticamente via config vars)
```

---

## Decisões Arquiteturais

| Decisão | Justificativa |
|---------|--------------|
| `spring.jpa.open-in-view=false` | Evita lazy loading fora da transação (anti-pattern) |
| `FetchType.EAGER` em User.roles | Roles são sempre necessárias para segurança; evita `LazyInitializationException` |
| `@Transactional(readOnly = true)` em reads | Performance (sem dirty checking, flush mode NEVER) |
| DTOs com construtores de entidade | Conversão direta, sem framework extra (ModelMapper, etc.) |
| `getUsername()` retorna email | Email é o identificador único de login no Spring Security |

---

## Pontos de Atenção e Dívidas Técnicas

> ⚠️ **`User.hasHole()`** — typo de `hasRole`. O método funciona, mas o nome é confuso.

> ⚠️ **`UserDTO`** — `@ApiModelProperty(position = 1)` duplicado. Campo `name` deveria ter `position = 2`.

> ℹ️ `Role` é a única entidade sem `LogFields` — não tem timestamps de auditoria.
