# Movieflix Backend — Árvore de Código-Fonte

## Visão Geral

Projeto monolith Spring Boot. Todo o código-fonte vive em `src/main/java/com/devgabriel/movieflix/`.

## Árvore Anotada

```
backend/
│
├── pom.xml                                    # Manifesto Maven — dependências, build, plugins
├── system.properties                          # Declara java.runtime.version=25 para Heroku
├── mvnw / mvnw.cmd                           # Maven Wrapper (sem necessidade de Maven local)
├── README.md                                  # Documentação original do projeto
│
├── images/
│   └── diagrama.png                           # Diagrama de classes do domínio
│
├── docs/                                      # Documentação do projeto (esta pasta)
│   ├── ARCHITECTURE-GUIDELINES.md            # Guia de arquitetura detalhado
│   └── [demais arquivos gerados pelo bmad]
│
└── src/
    ├── main/
    │   ├── java/com/devgabriel/movieflix/    # Raiz do pacote Java
    │   │   │
    │   │   ├── MovieflixApplication.java      # ★ Entry point (@SpringBootApplication)
    │   │   │
    │   │   ├── common/
    │   │   │   └── LogFields.java             # @MappedSuperclass com campos de auditoria
    │   │   │                                  #   (createdAt, updatedAt — inferidos pelo JPA)
    │   │   │
    │   │   ├── config/                        # Configurações Spring (Beans, Security, Swagger)
    │   │   │   ├── AppConfig.java             # BCryptPasswordEncoder bean
    │   │   │   ├── SecurityConfig.java        # Filtro Spring Security 6, regras HTTP por role
    │   │   │   │                              # → /genres/** e /movies/** = autenticado
    │   │   │   │                              # → POST /reviews/** = MEMBER apenas
    │   │   │   ├── JwtService.java            # Serviço de assinatura/validação JWT (HMAC)
    │   │   │   └── SwaggerConfig.java         # Configuração da UI Swagger
    │   │   │
    │   │   ├── components/
    │   │   │
    │   │   ├── entities/                      # ★ Domínio JPA (mapeamento objeto-relacional)
    │   │   │   ├── Genre.java                 # @Entity tb_genre — herda LogFields
    │   │   │   ├── Movie.java                 # @Entity tb_movie — herda LogFields
    │   │   │   │                              #   @ManyToOne → Genre
    │   │   │   │                              #   @OneToMany → List<Review>
    │   │   │   ├── Review.java                # @Entity tb_review — herda LogFields
    │   │   │   │                              #   @ManyToOne → User
    │   │   │   │                              #   @ManyToOne → Movie
    │   │   │   ├── User.java                  # @Entity tb_user — herda LogFields
    │   │   │   │                              #   implements UserDetails (Spring Security)
    │   │   │   │                              #   getUsername() retorna email
    │   │   │   │                              #   @ManyToMany (EAGER) → Set<Role>
    │   │   │   └── Role.java                  # @Entity tb_role — NÃO herda LogFields
    │   │   │                                  #   implements GrantedAuthority (via getAuthority())
    │   │   │
    │   │   ├── dtos/                          # Transfer Objects (desacopla API do domínio)
    │   │   │   ├── GenreDTO.java              # id, name
    │   │   │   ├── MovieDTO.java              # id, title, subTitle, year, imgUrl,
    │   │   │   │                              # synopsis, genreId ← achatado de genre.id
    │   │   │   │                              # List<ReviewDTO> reviews
    │   │   │   ├── ReviewDTO.java             # id, text, movieId ← achatado, UserDTO user
    │   │   │   ├── RoleDTO.java               # id, authority
    │   │   │   └── UserDTO.java               # id, name, email, Set<RoleDTO> roles
    │   │   │                                  #   ⚠ password NUNCA exposto
    │   │   │
    │   │   ├── repositories/                  # ★ Spring Data JPA (interfaces JPA)
    │   │   │   ├── GenreRepository.java       # JpaRepository<Genre, Long>
    │   │   │   ├── MovieRepository.java       # JpaRepository<Movie, Long>
    │   │   │   ├── ReviewRepository.java      # JpaRepository<Review, Long>
    │   │   │   ├── RoleRepository.java        # JpaRepository<Role, Long>
    │   │   │   └── UserRepository.java        # JpaRepository<User, Long>
    │   │   │                                  #   findByEmail(String) — usado pelo UserDetailsService
    │   │   │
    │   │   ├── services/                      # ★ Lógica de negócio (@Service, @Transactional)
    │   │   │   ├── AuthService.java           # authenticated() — obtém User autenticado do contexto
    │   │   │   ├── GenreService.java          # findAll() → List<GenreDTO>
    │   │   │   ├── MovieService.java          # findAll(Pageable) → Page<MovieDTO>
    │   │   │   │                              # findById(Long) → MovieDTO (com reviews)
    │   │   │   ├── ReviewsService.java        # insert(ReviewDTO) → ReviewDTO
    │   │   │   │                              # ↳ vincula ao User autenticado via AuthService
    │   │   │   ├── UserService.java           # implements UserDetailsService
    │   │   │   │                              # loadUserByUsername(email) → UserDetails
    │   │   │   └── exceptions/
    │   │   │       ├── ForbiddenException.java         # HTTP 403
    │   │   │       ├── ResourceNotFoundException.java  # HTTP 404
    │   │   │       └── UnauthorizedException.java      # HTTP 401
    │   │   │
    │   │   └── resources/                     # ★ Controllers REST (@RestController)
    │   │       ├── GenreResource.java         # GET /genres → List<GenreDTO>
    │   │       │                              #   @PreAuthorize: qualquer autenticado
    │   │       ├── MovieResource.java         # GET /movies → Page<MovieDTO>
    │   │       │                              # GET /movies/{id} → MovieDTO
    │   │       │                              #   @PreAuthorize: qualquer autenticado
    │   │       ├── ReviewResource.java        # POST /reviews → ReviewDTO (201)
    │   │       │                              #   @PreAuthorize: ROLE_MEMBER apenas
    │   │       └── exceptions/
    │   │           ├── ResourceExceptionHandler.java  # @ControllerAdvice — captura exceções
    │   │           ├── StandardError.java             # Corpo de erro padrão (timestamp, status, error, message, path)
    │   │           ├── ValidationError.java           # Extends StandardError + List<FieldMessage>
    │   │           ├── FieldMessage.java              # fieldName + message
    │   │
    │   └── resources/                         # Configurações e dados de seed
    │       ├── application.properties         # spring.profiles.active=${APP_PROFILE:test}
    │       │                                  # spring.jpa.open-in-view=false
    │       ├── application-test.properties    # H2 datasource, DDL auto-create, H2 console ativo
    │       ├── application-dev.properties     # PostgreSQL local (porta 5432, db: movieflix)
    │       ├── application-prod.properties    # PostgreSQL Heroku (via DATABASE_URL)
    │       └── data.sql                       # Seed: roles, usuários, gêneros, filmes, reviews
    │
    └── test/
        └── java/com/devgabriel/movieflix/
            ├── MovieflixApplicationTests.java          # Context load test
            └── tests/web/it/                          # Testes de integração (MockMvc + JWT)
                ├── GenreResourceIT.java               # 3 cenários: 401, VISITOR 200, MEMBER 200
                ├── MovieResourceIT.java               # 6 cenários: 401, 200, 404
                └── ReviewResourceIT.java              # 4 cenários: 401, 403, 201, 422
```

## Diretórios Críticos

| Diretório | Propósito |
|-----------|-----------|
| `src/main/java/.../entities/` | Domínio de negócio — mapeamento JPA |
| `src/main/java/.../services/` | Lógica de negócio — ponto central de transações |
| `src/main/java/.../resources/` | Controllers REST — interface HTTP |
| `src/main/java/.../config/` | Segurança JWT, Swagger, Beans globais |
| `src/main/java/.../dtos/` | Contratos da API — desacopla domínio da camada HTTP |
| `src/main/java/.../repositories/` | Acesso a dados via Spring Data JPA |
| `src/main/resources/` | Configurações por perfil (test/dev/prod) e seed SQL |
| `src/test/java/.../tests/web/it/` | Testes de integração com MockMvc |

## Entry Points

| Arquivo | Propósito |
|---------|-----------|
| `MovieflixApplication.java` | Main class — inicializa o contexto Spring |
| `SecurityConfig.java` | Define regras de segurança, Stateless Session e rotas |

## Padrão de Nomenclatura de Arquivos

| Sufixo | Tipo | Exemplo |
|--------|------|---------|
| (sem sufixo) | Entity JPA | `Movie.java` |
| `DTO` | Data Transfer Object | `MovieDTO.java` |
| `Repository` | Spring Data JPA | `MovieRepository.java` |
| `Service` | Serviço de negócio | `MovieService.java` |
| `Resource` | Controller REST | `MovieResource.java` |
| `Config` | Configuração Spring | `SecurityConfig.java` |
| `IT` | Integration Test | `MovieResourceIT.java` |
