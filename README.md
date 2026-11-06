# catalogo-api
API REST de catálogo de produtos (Java 21, Spring Boot 4, PostgreSQL, Flyway, Spring Data JPA).

## Como rodar
1. Copie `.env.example` para `.env` e preencha usuário, senha e nome do banco.
2. `docker compose up -d` (sobe o PostgreSQL)
3. `./mvnw spring-boot:run` (API em http://localhost:8081)

## Endpoints
| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| GET | /produtos?ativo=true&page=0&size=10 | Lista paginada | 200 |
| GET | /produtos/{id} | Busca por id | 200 / 404 |
| POST | /produtos | Cria | 201 + Location |
| PUT | /produtos/{id} | Atualiza | 200 / 404 |
| DELETE | /produtos/{id} | Desativa (soft delete) | 204 / 404 |

## Decisões
- DTOs em `record`; a entidade não é exposta na API.
- DELETE é lógico (`ativo = false`).
- Schema versionado com Flyway; `ddl-auto=validate`.

## Próximos passos
Validação, tratamento global de erros, testes, Spring Security + JWT.