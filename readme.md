# Documentação Microserviços
https://claude.ai/code/artifact/2c541174-0b49-486d-9ee2-1682e91b8daf

# Documentação implementação Docker GUIA
https://claude.ai/code/artifact/a5f3c8e5-81de-4791-a16c-a71d4aa45f0f

# Documentação implementação Kubernetes GUIA
https://claude.ai/code/artifact/68cd2944-af3d-430f-8e6f-ea27c4186982

### Como rodar
```
docker compose up 
```

#### Registrar

```
curl --request POST \
  --url http://localhost:8085/auth-service/register \
  --header 'Content-Type: application/json' \
  --data '{
  "name": "Test",
  "email": "email@test.com",
  "password": "password@123"
}'
```

#### Autenticação

```
curl --request POST \
  --url http://localhost:8085/auth-service/login \
  --header 'Content-Type: application/json' \
  --data '{
  "email": "email@test.com",
  "password": "password@123"
}'
```

#### Refresh

Substitua <token_gerado> pelo token obtido no loign. 
```
curl --request POST \
  --url http://localhost:8085/auth-service/refresh \
  --header 'Authorization: Bearer <token_gerado>' \
  --header 'Content-Type: application/json' \
  --data '{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEB0ZXN0LmNvbSIsImlhdCI6MTc5MDA5OTA3MCwiZXhwIjoxNzkwNzAzODcwfQ.MM-39U_hkEoK6emANYVAqr5-OPTe0t_ZEl3Ble7sZ2c"
}'
```

#### Testar autenticação

Substitua <token_gerado> pelo token obtido no loign. Sem o token a requisição retorna com 401 (Unauthorized)
```
curl --request GET \
--url http://localhost:8085/clientes-service/clientes \
--header 'Authorization: Bearer <token_gerado>'
```