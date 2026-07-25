# Movieflix Backend — Contratos de API

## Visão Geral

API REST protegida por JWT (Bearer Token). Todos os endpoints requerem autenticação. Retorna JSON.

- **Base URL (local):** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Content-Type:** `application/json`

---

## Autenticação JWT Stateless

### Obter Token de Acesso

```
POST /auth/login
```

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "bob@gmail.com",
  "password": "123456"
}
```

**Resposta (200 OK):**
```json
{
  "access_token": "<JWT>",
  "token_type": "Bearer",
  "expires_in": 86400
}
```

> O campo `sub` no token contém o email, e a role fica no claim `roles`.

**Usar o token:**
```
Authorization: Bearer <access_token>
```

---

## Endpoints de Gêneros

### Listar todos os gêneros

```
GET /genres
```

**Roles permitidas:** VISITOR, MEMBER (qualquer autenticado)

**Resposta (200 OK):**
```json
[
  { "id": 1, "name": "Ação" },
  { "id": 2, "name": "Comédia" },
  { "id": 3, "name": "Drama" },
  { "id": 4, "name": "Ficção Científica" },
  { "id": 5, "name": "Romance" }
]
```

**Erros:**
| Status | Condição |
|--------|----------|
| 401 | Token ausente ou inválido |

---

## Endpoints de Filmes

### Listar filmes (paginado)

```
GET /movies
GET /movies?page=0&size=12&sort=title,asc
```

**Roles permitidas:** VISITOR, MEMBER (qualquer autenticado)

**Parâmetros de query (opcionais):**
| Parâmetro | Tipo | Padrão | Descrição |
|-----------|------|--------|-----------|
| `page` | integer | 0 | Página (0-indexed) |
| `size` | integer | 12 | Itens por página |
| `sort` | string | — | Campo e direção (ex: `title,asc`) |

**Resposta (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Missão impossível",
      "subTitle": "...",
      "year": 1996,
      "imgUrl": "https://...",
      "synopsis": "...",
      "genreId": 1,
      "reviews": []
    }
  ],
  "pageable": { ... },
  "totalElements": 10,
  "totalPages": 1,
  "last": true,
  "first": true,
  "size": 12,
  "number": 0
}
```

**Erros:**
| Status | Condição |
|--------|----------|
| 401 | Token ausente ou inválido |

---

### Buscar filme por ID (com reviews)

```
GET /movies/{id}
```

**Roles permitidas:** VISITOR, MEMBER (qualquer autenticado)

**Path Parameters:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `id` | Long | ID do filme |

**Resposta (200 OK):**
```json
{
  "id": 1,
  "title": "Missão impossível",
  "subTitle": "Mission: Impossible",
  "year": 1996,
  "imgUrl": "https://...",
  "synopsis": "Descrição do filme...",
  "genreId": 1,
  "reviews": [
    {
      "id": 1,
      "text": "Adorei o filme!",
      "movieId": 1,
      "user": {
        "id": 2,
        "name": "Bob",
        "email": "bob@gmail.com",
        "roles": [
          { "id": 1, "authority": "VISITOR" },
          { "id": 2, "authority": "MEMBER" }
        ]
      }
    }
  ]
}
```

**Erros:**
| Status | Condição |
|--------|----------|
| 401 | Token ausente ou inválido |
| 404 | Filme não encontrado |

**Exemplo de erro 404:**
```json
{
  "timestamp": "2026-07-25T12:00:00Z",
  "status": 404,
  "error": "Resource not found",
  "message": "Id not found: 100",
  "path": "/movies/100"
}
```

---

## Endpoints de Reviews

### Criar review

```
POST /reviews
```

**Roles permitidas:** MEMBER **apenas** (VISITOR recebe 403)

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:**
```json
{
  "text": "Filme incrível, adorei!",
  "movieId": 1
}
```

**Validações:**
| Campo | Regra | Erro |
|-------|-------|------|
| `text` | Não pode ser vazio/branco (`@NotBlank`) | 422 |
| `movieId` | Deve referenciar filme existente | 404 |

**Resposta (201 Created):**
```json
{
  "id": 6,
  "text": "Filme incrível, adorei!",
  "movieId": 1,
  "user": {
    "id": 2,
    "name": "Bob",
    "email": "bob@gmail.com",
    "roles": [
      { "id": 1, "authority": "VISITOR" },
      { "id": 2, "authority": "MEMBER" }
    ]
  }
}
```

**Erros:**
| Status | Condição |
|--------|----------|
| 401 | Token ausente ou inválido |
| 403 | Usuário VISITOR tentando criar review |
| 422 | Validação falhou (`text` em branco) |

**Exemplo de erro 422:**
```json
{
  "timestamp": "2026-07-25T12:00:00Z",
  "status": 422,
  "error": "Validation exception",
  "message": "Validation error",
  "path": "/reviews",
  "errors": [
    { "fieldName": "text", "message": "must not be blank" }
  ]
}
```

---

## Resumo de Permissões

| Endpoint | Método | VISITOR | MEMBER | Anônimo |
|----------|--------|---------|--------|---------|
| `/genres` | GET | ✅ 200 | ✅ 200 | ❌ 401 |
| `/movies` | GET | ✅ 200 | ✅ 200 | ❌ 401 |
| `/movies/{id}` | GET | ✅ 200 | ✅ 200 | ❌ 401 |
| `/reviews` | POST | ❌ 403 | ✅ 201 | ❌ 401 |

---

## Exemplos com cURL

```bash
# 1. Obter token (como MEMBER - Bob)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"bob@gmail.com","password":"123456"}'

# 2. Listar gêneros
curl -H "Authorization: Bearer <TOKEN>" \
  http://localhost:8080/genres

# 3. Listar filmes
curl -H "Authorization: Bearer <TOKEN>" \
  http://localhost:8080/movies

# 4. Buscar filme por ID
curl -H "Authorization: Bearer <TOKEN>" \
  http://localhost:8080/movies/1

# 5. Criar review (requer MEMBER)
curl -X POST \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"text":"Filme incrível!","movieId":1}' \
  http://localhost:8080/reviews
```
