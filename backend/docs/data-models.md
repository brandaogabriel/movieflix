# Movieflix Backend — Modelos de Dados

## Diagrama Entidade-Relacionamento

```
┌─────────────┐         ┌──────────────────────────────────────────┐
│  tb_genre   │         │  tb_movie                                │
│─────────────│         │──────────────────────────────────────────│
│ id (PK)     │◄────────│ id (PK)                                  │
│ name        │  1:N    │ title                                    │
│ created_at  │         │ sub_title                                │
│ updated_at  │         │ year                                     │
└─────────────┘         │ img_url                                  │
                        │ synopsis                                 │
                        │ genre_id (FK → tb_genre)                 │
                        │ created_at                               │
                        │ updated_at                               │
                        └──────────────────────────────────────────┘
                                          │
                                          │ 1:N
                                          ▼
┌─────────────┐         ┌──────────────────────────────────────────┐
│  tb_user    │         │  tb_review                               │
│─────────────│         │──────────────────────────────────────────│
│ id (PK)     │◄────────│ id (PK)                                  │
│ name        │  1:N    │ text (TEXT)                              │
│ email       │         │ user_id (FK → tb_user)                   │
│ password    │         │ movie_id (FK → tb_movie)                 │
│ created_at  │         │ created_at                               │
│ updated_at  │         │ updated_at                               │
└─────────────┘         └──────────────────────────────────────────┘
       │
       │ N:M (via tb_user_role)
       ▼
┌─────────────┐
│  tb_role    │
│─────────────│
│ id (PK)     │
│ authority   │
└─────────────┘
```

---

## Tabelas do Banco de Dados

### `tb_genre` — Gêneros

| Coluna | Tipo | Restrições | Descrição |
|--------|------|-----------|-----------|
| `id` | BIGINT | PK, IDENTITY | Identificador único |
| `name` | VARCHAR | NOT NULL | Nome do gênero |
| `created_at` | TIMESTAMP | — | Herdado de LogFields |
| `updated_at` | TIMESTAMP | — | Herdado de LogFields |

**Índices:** PK em `id`

---

### `tb_movie` — Filmes

| Coluna | Tipo | Restrições | Descrição |
|--------|------|-----------|-----------|
| `id` | BIGINT | PK, IDENTITY | Identificador único |
| `title` | VARCHAR | NOT NULL | Título principal |
| `sub_title` | VARCHAR | — | Subtítulo / título original |
| `year` | INTEGER | — | Ano de lançamento |
| `img_url` | VARCHAR | — | URL da imagem de capa |
| `synopsis` | TEXT | — | Sinopse do filme |
| `genre_id` | BIGINT | FK → tb_genre | Gênero do filme |
| `created_at` | TIMESTAMP | — | Herdado de LogFields |
| `updated_at` | TIMESTAMP | — | Herdado de LogFields |

**Índices:** PK em `id`, FK em `genre_id`

---

### `tb_review` — Avaliações

| Coluna | Tipo | Restrições | Descrição |
|--------|------|-----------|-----------|
| `id` | BIGINT | PK, IDENTITY | Identificador único |
| `text` | TEXT | NOT NULL, NOT BLANK | Texto da avaliação |
| `user_id` | BIGINT | FK → tb_user | Usuário que escreveu |
| `movie_id` | BIGINT | FK → tb_movie | Filme avaliado |
| `created_at` | TIMESTAMP | — | Herdado de LogFields |
| `updated_at` | TIMESTAMP | — | Herdado de LogFields |

**Índices:** PK em `id`, FK em `user_id` e `movie_id`

---

### `tb_user` — Usuários

| Coluna | Tipo | Restrições | Descrição |
|--------|------|-----------|-----------|
| `id` | BIGINT | PK, IDENTITY | Identificador único |
| `name` | VARCHAR | NOT NULL | Nome de exibição |
| `email` | VARCHAR | UNIQUE, NOT NULL | Login (usado para autenticação JWT) |
| `password` | VARCHAR | NOT NULL | Senha BCrypt-encoded |
| `created_at` | TIMESTAMP | — | Herdado de LogFields |
| `updated_at` | TIMESTAMP | — | Herdado de LogFields |

**Índices:** PK em `id`, UNIQUE em `email`

> ⚠️ `password` **nunca** é exposto na API (ausente em `UserDTO`)

---

### `tb_role` — Perfis de Acesso

| Coluna | Tipo | Restrições | Descrição |
|--------|------|-----------|-----------|
| `id` | BIGINT | PK, IDENTITY | Identificador único |
| `authority` | VARCHAR | NOT NULL | Nome do perfil (ex: `VISITOR`, `MEMBER`) |

> ⚠️ `tb_role` é a **única entidade que NÃO herda de LogFields** — sem campos de auditoria.

---

### `tb_user_role` — Relacionamento N:M Usuário-Perfil

| Coluna | Tipo | Restrições | Descrição |
|--------|------|-----------|-----------|
| `user_id` | BIGINT | FK → tb_user | Usuário |
| `role_id` | BIGINT | FK → tb_role | Perfil |

**Chave primária composta:** (`user_id`, `role_id`)

---

## Entidades Java (Domain Model)

### `Genre`
```java
@Entity @Table(name = "tb_genre")
class Genre extends LogFields {
    Long id;
    String name;
    List<Movie> movies;  // @OneToMany(mappedBy = "genre") — final
}
```

### `Movie`
```java
@Entity @Table(name = "tb_movie")
class Movie extends LogFields {
    Long id;
    String title, subTitle, imgUrl, synopsis;
    Integer year;
    Genre genre;        // @ManyToOne
    List<Review> reviews; // @OneToMany(mappedBy = "movie") — final
}
```

### `Review`
```java
@Entity @Table(name = "tb_review")
class Review extends LogFields {
    Long id;
    String text;        // @Column(columnDefinition = "TEXT")
    User user;          // @ManyToOne @JoinColumn(name = "user_id")
    Movie movie;        // @ManyToOne @JoinColumn(name = "movie_id")
}
```

### `User`
```java
@Entity @Table(name = "tb_user")
class User extends LogFields implements UserDetails {
    Long id;
    String name, email, password;
    List<Review> reviews; // @OneToMany(mappedBy = "user") — final
    Set<Role> roles;      // @ManyToMany(fetch = EAGER) via tb_user_role — final
    // getUsername() → retorna email!
}
```

### `Role`
```java
@Entity @Table(name = "tb_role")
class Role implements Serializable {  // ← sem LogFields
    Long id;
    String authority;
    // getAuthority() → authority (ex: "VISITOR", "MEMBER")
}
```

### `LogFields` (Superclasse de auditoria)
```java
@MappedSuperclass
abstract class LogFields implements Serializable {
    // Campos de auditoria (createdAt, updatedAt)
}
```

---

## Convenções

| Convenção | Regra |
|-----------|-------|
| Tabelas | Prefixo `tb_` + nome no singular |
| Join Tables | `tb_` + entidade1 + `_` + entidade2 |
| ID Strategy | `GenerationType.IDENTITY` em todas as entidades |
| `equals/hashCode` | Baseados **apenas no campo `id`** |
| Coleções `@OneToMany` | Sempre `final` + inicializadas inline (`new ArrayList<>()`) |
| Roles em `User` | `Set<Role>` com `FetchType.EAGER` |

---

## Dados de Seed (perfil `test`)

### Roles
| ID | Authority |
|----|-----------|
| 1 | VISITOR |
| 2 | MEMBER |

### Usuários (senha: `123456` — BCrypt)
| ID | Nome | Email | Roles |
|----|------|-------|-------|
| 1 | Ana | ana@gmail.com | VISITOR |
| 2 | Bob | bob@gmail.com | VISITOR, MEMBER |
| 3 | Admin | admin@gmail.com | VISITOR, MEMBER |

### Gêneros
| ID | Nome |
|----|------|
| 1 | Ação |
| 2 | Comédia |
| 3 | Drama |
| 4 | Ficção Científica |
| 5 | Romance |

### Filmes (10 registros)
| ID | Título | Gênero |
|----|--------|--------|
| 1 | Missão impossível | Ação |
| 2 | John Wick | Ação |
| 3 | O auto da compadecida | Comédia |
| 4 | As branquelas | Comédia |
| 5 | Matrix | Ficção Científica |
| 6 | A culpa é das estrelas | Romance |
| 7 | Como eu era antes de você | Romance |
| 8 | Até o último homem | Ação |
| 9 | Coringa | Drama |
| 10 | De volta para o futuro | Ficção Científica |
