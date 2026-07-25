# Movieflix Backend — Índice de Documentação

> **Gerado em:** 2026-07-25 | **Scan Level:** Quick | **Modo:** initial_scan  
> Este arquivo é o **ponto de entrada principal** para IA assistida e desenvolvimento humano.

---

## Visão Geral do Projeto

| Campo | Valor |
|-------|-------|
| **Tipo** | Monolith Backend |
| **Linguagem** | Java 25 |
| **Framework** | Spring Boot 3.5.3 |
| **Segurança** | JWT Stateless (Spring Security 6) |
| **Banco (test)** | H2 in-memory |
| **Banco (prod)** | PostgreSQL |
| **Arquitetura** | Layered (Resource → Service → Repository) |
| **Entry Point** | `MovieflixApplication.java` |
| **Raiz do projeto** | `/src/main/java/com/devgabriel/movieflix/` |

---

## Referência Rápida

### Endpoints da API

| Método | Endpoint | Role Mínima | Descrição |
|--------|----------|------------|-----------|
| POST | `/auth/login` | — | Autenticar (obter JWT) |
| GET | `/genres` | VISITOR | Listar gêneros |
| GET | `/movies` | VISITOR | Listar filmes (paginado) |
| GET | `/movies/{id}` | VISITOR | Buscar filme com reviews |
| POST | `/reviews` | MEMBER | Criar avaliação |

### Usuários de Seed (perfil `test`)

| Email | Senha | Role |
|-------|-------|------|
| ana@gmail.com | 123456 | VISITOR |
| bob@gmail.com | 123456 | VISITOR + MEMBER |
| admin@gmail.com | 123456 | VISITOR + MEMBER |

### Comandos Rápidos

```bash
# Iniciar (H2 in-memory)
./mvnw spring-boot:run

# Testes
./mvnw test

# Build JAR
./mvnw clean package
```

---

## Documentação Gerada

| Documento | Descrição |
|-----------|-----------|
| [Visão Geral do Projeto](./project-overview.md) | Stack, estrutura, como iniciar |
| [Arquitetura](./architecture.md) | Padrões, fluxo de requisição, segurança JWT Stateless |
| [Contratos de API](./api-contracts.md) | Endpoints, schemas, exemplos cURL |
| [Modelos de Dados](./data-models.md) | Entidades JPA, tabelas, relacionamentos, seed |
| [Árvore de Código-Fonte](./source-tree-analysis.md) | Estrutura de diretórios anotada |
| [Guia de Desenvolvimento](./development-guide.md) | Setup, build, testes, convenções |
| [Guia de Deploy](./deployment-guide.md) | Heroku, PostgreSQL, variáveis de ambiente |

---

## Documentação Existente (Original)

| Documento | Descrição |
|-----------|-----------|
| [ARCHITECTURE-GUIDELINES.md](./ARCHITECTURE-GUIDELINES.md) | Guia de arquitetura completo e detalhado (original do projeto) |
| [README.md](../README.md) | Documentação original do repositório |

---

## Como Usar esta Documentação

### Para desenvolvimento de novas features

1. Leia [architecture.md](./architecture.md) para entender o padrão de camadas
2. Consulte [data-models.md](./data-models.md) para entender o domínio
3. Consulte [api-contracts.md](./api-contracts.md) para os contratos HTTP
4. Siga o checklist em [development-guide.md](./development-guide.md)

### Para debug e investigação

1. Consulte [source-tree-analysis.md](./source-tree-analysis.md) para localizar arquivos
2. Consulte [ARCHITECTURE-GUIDELINES.md](./ARCHITECTURE-GUIDELINES.md) para detalhes profundos

### Para planejamento de evolução (PRD)

Aponte o workflow de PRD para este `index.md` como entrada.

---

## Estrutura de Pacotes (Referência Rápida)

```
com.devgabriel.movieflix
├── config/         ← Segurança JWT, Swagger, Beans
├── components/     ← Componentes utilitários
├── common/         ← LogFields (auditoria)
├── entities/       ← Genre, Movie, Review, User, Role
├── dtos/           ← GenreDTO, MovieDTO, ReviewDTO, UserDTO, RoleDTO
├── repositories/   ← Spring Data JPA
├── services/       ← Lógica de negócio (@Transactional)
└── resources/      ← Controllers REST + exception handlers
```

---

## Pontos de Atenção

> ⚠️ `User.hasHole()` — typo de `hasRole`. Funciona mas nome é confuso.  
> ⚠️ `UserDTO` — `@ApiModelProperty(position = 1)` duplicado nos campos `id` e `name`.  
> ℹ️ `Role` não herda de `LogFields` — sem campos de auditoria.  
> ℹ️ `spring.jpa.open-in-view=false` — todo lazy loading deve ser resolvido dentro de `@Transactional` nos serviços.
