# Movieflix Backend — Guia de Deploy

## Ambientes Disponíveis

| Ambiente | Banco | Perfil Spring | Ativação |
|----------|-------|--------------|----------|
| **test** | H2 in-memory | `test` | Padrão (sem variável) |
| **dev** | PostgreSQL local | `dev` | `APP_PROFILE=dev` |
| **prod** | PostgreSQL Heroku | `prod` | Heroku config var |

---

## Deploy no Heroku

O projeto está configurado para deploy no Heroku com as seguintes configurações.

### Arquivos de configuração

**`system.properties`** — declara a versão do Java:
```properties
java.runtime.version=11
```

**`application-prod.properties`** — datasource Heroku:
```properties
spring.datasource.url=${DATABASE_URL}
# Heroku provisiona PostgreSQL automaticamente via DATABASE_URL
```

### Passos para Deploy

```bash
# 1. Login no Heroku
heroku login

# 2. Criar aplicação (se ainda não existir)
heroku create nome-da-sua-app

# 3. Adicionar add-on PostgreSQL
heroku addons:create heroku-postgresql:mini

# 4. Configurar o perfil de produção
heroku config:set APP_PROFILE=prod

# 5. Deploy via Git
git push heroku main

# 6. Verificar logs
heroku logs --tail
```

### Variáveis de Ambiente no Heroku

| Variável | Valor | Configuração |
|----------|-------|-------------|
| `APP_PROFILE` | `prod` | Manual via `heroku config:set` |
| `DATABASE_URL` | URL PostgreSQL | Automático pelo add-on |
| `JAVA_TOOL_OPTIONS` | (opcional) | Para tuning JVM |

---

## Deploy via JAR (qualquer servidor)

```bash
# 1. Build do JAR
./mvnw clean package -DskipTests

# 2. Executar em produção
java -jar target/movieflix-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://host:5432/movieflix \
  --spring.datasource.username=postgres \
  --spring.datasource.password=senha
```

---

## Configurações de Banco de Dados por Ambiente

### `application-test.properties`
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

### `application-dev.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/movieflix
spring.datasource.username=postgres
spring.datasource.password=<senha>
spring.jpa.hibernate.ddl-auto=update
```

### `application-prod.properties`
```properties
spring.datasource.url=${DATABASE_URL}
spring.jpa.hibernate.ddl-auto=validate
```

---

## Verificação pós-deploy

```bash
# Health check (se endpoint disponível)
curl https://sua-app.herokuapp.com/genres \
  -H "Authorization: Bearer <TOKEN>"

# Obter token de teste
curl -X POST https://sua-app.herokuapp.com/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"bob@gmail.com","password":"123456"}'
```

---

## Considerações de Segurança para Produção

> ⚠️ **Antes de ir para produção, revise:**

1. **Client secret** `movieflix123` deve ser alterado para um valor seguro
2. **Senha dos usuários seed** — não usar `123456` em produção
3. **JWT signing key** — deve ser uma chave forte e secreta
4. **CORS** — configurar origens permitidas
5. **HTTPS** — Heroku fornece SSL automaticamente
6. **Logs** — revisar o que está sendo logado (sem dados sensíveis)
