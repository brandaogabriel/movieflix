# Movieflix Backend — Guia de Desenvolvimento

## Pré-requisitos

| Ferramenta | Versão | Notas |
|-----------|--------|-------|
| **Java JDK** | 25 | `java -version` para verificar |
| **Maven** | 3.6+ | ou use o Maven Wrapper incluso (`./mvnw`) |
| **Git** | qualquer | — |
| **PostgreSQL** | 12+ | **apenas** para perfil `dev` |
| **cURL ou Postman** | qualquer | para testar a API |

> O projeto inclui **Maven Wrapper** (`mvnw` / `mvnw.cmd`) — não é necessário instalar Maven globalmente.

---

## Setup Rápido (Ambiente de Teste)

O perfil padrão usa **H2 in-memory** — nenhum banco externo é necessário.

```bash
# 1. Clonar o repositório
git clone <repo-url>
cd backend

# 2. Build e execução
./mvnw spring-boot:run
# ou: mvn spring-boot:run

# 3. API disponível em:
# http://localhost:8080
# http://localhost:8080/h2-console     (console H2)
# http://localhost:8080/swagger-ui.html (Swagger UI)
```

---

## Configuração do H2 Console

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:testdb` |
| User Name | `sa` |
| Password | (vazio) |

---

## Setup para Ambiente de Desenvolvimento (PostgreSQL)

```bash
# 1. Criar banco de dados
psql -U postgres
CREATE DATABASE movieflix;

# 2. Configurar application-dev.properties (já existente)
# spring.datasource.url=jdbc:postgresql://localhost:5432/movieflix
# spring.datasource.username=postgres
# spring.datasource.password=<sua-senha>

# 3. Executar com perfil dev
APP_PROFILE=dev ./mvnw spring-boot:run
```

---

## Comandos de Build

```bash
# Compilar e instalar no repositório local Maven
./mvnw install

# Compilar sem testes
./mvnw install -DskipTests

# Limpar build anterior
./mvnw clean

# Build completo (clean + install)
./mvnw clean install

# Gerar JAR executável
./mvnw package
# Resultado: target/movieflix-0.0.1-SNAPSHOT.jar
```

---

## Comandos de Execução

```bash
# Perfil test (padrão — H2 in-memory)
./mvnw spring-boot:run

# Perfil dev (PostgreSQL local)
APP_PROFILE=dev ./mvnw spring-boot:run

# Perfil prod
APP_PROFILE=prod ./mvnw spring-boot:run

# Via JAR gerado
java -jar target/movieflix-0.0.1-SNAPSHOT.jar
```

---

## Comandos de Teste

```bash
# Executar todos os testes
./mvnw test

# Executar testes de uma classe específica
./mvnw test -Dtest=MovieResourceIT

# Executar testes com relatório de cobertura (se Jacoco configurado)
./mvnw verify
```

### Testes Disponíveis

| Arquivo | Tipo | Descrição |
|---------|------|-----------|
| `MovieflixApplicationTests` | Context Load | Valida que o contexto Spring sobe sem erros |
| `GenreResourceIT` | Integração | 3 cenários: 401, VISITOR 200, MEMBER 200 |
| `MovieResourceIT` | Integração | 6 cenários: 401, 200, 404 |
| `ReviewResourceIT` | Integração | 4 cenários: 401, 403, 201, 422 |

Os testes de integração:
- Usam `@SpringBootTest` + `@AutoConfigureMockMvc` + `@Transactional`
- Obtêm tokens JWT via utilitário interno `TokenUtil.getAccessToken()`
- Executam contra H2 in-memory (perfil `test`)

---

## Autenticação para Desenvolvimento/Testes

### Usuários de Seed

| Usuário | Email | Senha | Roles |
|---------|-------|-------|-------|
| Ana | ana@gmail.com | 123456 | VISITOR |
| Bob | bob@gmail.com | 123456 | VISITOR, MEMBER |
| Admin | admin@gmail.com | 123456 | VISITOR, MEMBER |

### Obter Token via cURL

```bash
# Token para VISITOR (Ana)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@gmail.com","password":"123456"}'

# Token para MEMBER (Bob)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"bob@gmail.com","password":"123456"}'
```

---

## Estrutura dos Pacotes Java

```
com.devgabriel.movieflix
├── common/         # LogFields (@MappedSuperclass)
├── components/     # JwtTokenEnhancer
├── config/         # Spring Security, JWT, Swagger
├── dtos/           # Data Transfer Objects
├── entities/       # Entidades JPA
├── repositories/   # Spring Data JPA
├── resources/      # Controllers REST + tratamento de exceções
└── services/       # Lógica de negócio + exceções de serviço
```

---

## Convenções de Código

### Nomenclatura

| Elemento | Convenção | Exemplo |
|----------|-----------|---------|
| Tabelas | `tb_` + nome singular | `tb_genre`, `tb_movie` |
| Controllers | `*Resource` | `GenreResource` |
| DTOs | `*DTO` | `GenreDTO` |
| Exceções de serviço | `services.exceptions.*` | `ResourceNotFoundException` |
| Exceções de recurso | `resources.exceptions.*` | `StandardError` |
| Testes de integração | `*IT` | `GenreResourceIT` |

### Padrões

1. **Entidades** implementam `Serializable` e `equals/hashCode` baseados em `id`
2. **DTOs** têm construtores que recebem a entidade para conversão
3. **Serviços de leitura** usam `@Transactional(readOnly = true)`
4. **Serviços de escrita** usam `@Transactional`
5. **Controllers** retornam `ResponseEntity<T>` com status HTTP explícito
6. **Coleções `@OneToMany`** são `final` e inicializadas inline
7. **Passwords** nunca expostos em DTOs

---

## Variáveis de Ambiente

| Variável | Valor padrão | Descrição |
|----------|-------------|-----------|
| `APP_PROFILE` | `test` | Perfil Spring ativo (`test`, `dev`, `prod`) |
| `DATABASE_URL` | — | URL completa do PostgreSQL (Heroku format, perfil `prod`) |

---

## Adicionando um Novo Recurso

Siga este checklist ao adicionar uma nova entidade/feature:

1. **Entidade** — crie em `entities/`, herda `LogFields`, implementa `Serializable`
2. **DTO** — crie em `dtos/`, inclui construtor com a entidade
3. **Repository** — crie em `repositories/`, extend `JpaRepository<Entidade, Long>`
4. **Service** — crie em `services/`, anote `@Service`, use `@Transactional`
5. **Resource** — crie em `resources/`, anote `@RestController @RequestMapping`
6. **Regra de segurança** — atualize `ResourceServerConfig` se necessário
7. **Seed data** — adicione ao `data.sql` se necessário para testes
8. **Testes** — crie `*IT.java` em `tests/web/it/`
