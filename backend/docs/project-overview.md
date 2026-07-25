# Movieflix Backend — Visão Geral do Projeto

## Sumário Executivo

O **Movieflix** é o back-end do projeto final do Bootcamp DevSuperior. Trata-se de uma API REST Java que gerencia um catálogo de filmes com avaliações de usuários, protegida por JWT Stateless (Spring Security 6) com roles diferenciadas (VISITOR e MEMBER).

## Identificação

| Campo | Valor |
|-------|-------|
| **Nome** | Movieflix Backend |
| **Versão** | 0.0.1-SNAPSHOT |
| **Group ID** | com.devgabriel |
| **Artifact ID** | movieflix |
| **Tipo de Repositório** | Monolith |
| **Tipo de Projeto** | backend |

## Stack Tecnológica

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| **Linguagem** | Java | 25 |
| **Framework** | Spring Boot | 3.5.3 |
| **Segurança** | JWT Stateless (Spring Security 6) | Spring Boot Starter OAuth2 Resource Server |
| **Persistência** | Spring Data JPA / Hibernate | — |
| **Banco de dados (test)** | H2 (in-memory) | — |
| **Banco de dados (dev/prod)** | PostgreSQL | — |
| **Documentação API** | SpringDoc OpenAPI 3 | 2.8.18 |
| **Build** | Maven (Wrapper) | — |
| **Validação** | Spring Boot Validation (Bean Validation) | — |
| **Testes** | Spring Boot Test + Spring Security Test | — |
| **Deploy** | Heroku (`system.properties`) | Java 25 runtime |

## Tipo de Arquitetura

**Layered Architecture (Arquitetura em Camadas)**

```
Request → Resource (Controller) → Service → Repository → Banco de dados
```

Padrões arquiteturais utilizados:
- **DTO Pattern** — isolamento entre entidades e a API
- **Repository Pattern** — abstração de acesso a dados via Spring Data JPA
- **Controller Advice** — tratamento centralizado de exceções
- **JWT Resource Server** — proteção de endpoints por roles

## Estrutura do Repositório

```
backend/                           # Raiz do projeto (monolith)
├── pom.xml                        # Dependências Maven
├── system.properties              # Runtime Java para Heroku
├── mvnw / mvnw.cmd               # Maven Wrapper
├── README.md                      # Documentação original
├── images/                        # Diagrama de classes
├── docs/                          # Documentação gerada
└── src/
    ├── main/java/                 # Código-fonte principal
    ├── main/resources/            # Configurações e seed data
    └── test/java/                 # Testes de integração
```

## Perfis de Ambiente

| Perfil | Banco | Ativação |
|--------|-------|----------|
| `test` (padrão) | H2 in-memory | `APP_PROFILE` ausente |
| `dev` | PostgreSQL local | `APP_PROFILE=dev` |
| `prod` | PostgreSQL Heroku | `APP_PROFILE=prod` |

## Como Iniciar

```bash
# Ambiente test (padrão) — H2 in-memory
mvn spring-boot:run

# Endpoints disponíveis:
# API:       http://localhost:8080
# H2 Console: http://localhost:8080/h2-console
# Swagger:   http://localhost:8080/swagger-ui.html
```

## Links para Documentação Detalhada

- [Arquitetura](./architecture.md)
- [Contratos de API](./api-contracts.md)
- [Modelos de Dados](./data-models.md)
- [Árvore de Código-Fonte](./source-tree-analysis.md)
- [Guia de Desenvolvimento](./development-guide.md)
- [Guia de Deploy](./deployment-guide.md)
- [Guia de Arquitetura (original)](./ARCHITECTURE-GUIDELINES.md)
