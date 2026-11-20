# Refúgio 🌡️ — API REST (CitizenTech)

> Entrega de **DDD Java** da Global Solution FIAP 2026.2. A mesma modelagem de dados serve a disciplina de **Database**.

---

## Ficha da CitizenTech

| Campo | Conteúdo |
|-------|----------|
| **Nome da solução** | Refúgio |
| **Equipe** | CitizenTech |
| **Integrantes** | Artur Rodrigues (RM564309) · Daniel Duarte (RM562508) · Felippe Nascimento Silva (RM562123) · Kawan Oliveira Amorim (RM562197) · Matheus Hideki Doroszewski Yoshimura (RM564970) |
| **Cidade / bairro** | São Paulo / SP (bairros: Santana, Mooca, Capão Redondo, Brás, Pirituba, Sé) |
| **Cidadão beneficiado** | Idoso que mora sozinho / trabalhador de rua em bairro urbano |
| **Problema (1 frase)** | Ondas de calor extremo matam, principalmente idosos que moram sozinhos e trabalhadores de rua. |
| **Como funciona** | Alerta de calor por bairro (INMET) + check-in "Tô bem" com escalada de contatos (SMS) + mapa de Pontos de Refresco. |
| **O que esta parte faz** | API REST (Spring Boot + Oracle via JDBC puro) que persiste o domínio e executa a regra central: a **escalada do check-in**. |
| **Repositório** | https://github.com/hidekiys/refugio-api |
| **Deploy** | [https://refugio-api-cf2p.onrender.com](https://refugio-api-cf2p.onrender.com) |
| **Indicador de impacto** | Nº de idosos em risco detectados e escalados a tempo (idosos sem check-in em bairro com alerta vermelho). |
| **ODS** | 3 (Saúde) · 10 (Desigualdades) · 11 (Cidades sustentáveis) · 13 (Ação climática) |

---

## Arquitetura

Camadas (padrão FIAP, com toques de DDD):

```
config/      ConnectionFactory (JDBC via DriverManager, credenciais em env vars)
model/       entidades (POJOs) + model.vo (Coordenada, IndiceDeCalor) + model.enums
dao/         1 DAO por tabela — CRUD em JDBC PURO (Connection/PreparedStatement/ResultSet)
bo/          Business Objects — regras de negócio (a ESCALADA mora no CheckinBO)
controller/  @RestController — GET/POST/PUT/DELETE
exception/   ResourceNotFoundException (404), BusinessException (400), handler global
```

**Acesso a dados:** JDBC puro com o driver `ojdbc11`. **Sem** JPA / Hibernate / Spring Data.

### Regra de negócio central — escalada do check-in "Tô bem"

Para um check-in **sem resposta** (`data_hora_resposta` nula):

- passou de **2h** → status `ESCALADO_CUIDADOR` + preenche `data_hora_contato_cuidador`;
- passou de **4h** → status `ESCALADO_AGENTE` + preenche `data_hora_contato_agente`.

Implementada em `bo/CheckinBO.java` (`processarEscalada`, `executarEscaladaGlobal`).

---

## Pré-requisitos

- **JDK 21** (ex.: Microsoft OpenJDK 21). Build via **Maven Wrapper** (`mvnw`) — não precisa instalar Maven.
- **Oracle** acessível (local: Oracle 21c XE, PDB `XEPDB1`, usuário `iatotvs/iatotvs123`).
- Scripts do banco em `../database/` (`ddl_refugio.sql`, depois `dml_refugio.sql`).

---

## Variáveis de ambiente

A `ConnectionFactory` lê as credenciais do Oracle de variáveis de ambiente (com defaults para o XE local). No deploy (render.com), configure-as no painel do serviço.

| Variável | Default | Descrição |
|----------|---------|-----------|
| `DB_URL` | `jdbc:oracle:thin:@localhost:1521/XEPDB1` | URL JDBC do Oracle |
| `DB_USERNAME` | `iatotvs` | usuário do schema |
| `DB_PASSWORD` | `iatotvs123` | senha do schema |
| `PORT` | `8080` | porta HTTP (o render injeta automaticamente) |

---

## Como executar

### 1. Rodar em modo desenvolvimento

Windows (PowerShell):

```powershell
$env:JAVA_HOME="C:\Users\Matheus Hideki\.jdks\ms-21.0.11"
cd "D:\Documentos\Fiap\GS 2 2026\refugio-api"
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
export JAVA_HOME=/caminho/para/jdk-21
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

### 2. Gerar o JAR (produção)

```powershell
.\mvnw.cmd clean package -DskipTests
java -jar target/refugio-api-0.0.1-SNAPSHOT.jar
```

### 3. Docker (igual ao render.com)

```bash
docker build -t refugio-api .
docker run -p 8080:8080 \
  -e DB_URL="jdbc:oracle:thin:@host:1521/XEPDB1" \
  -e DB_USERNAME="usuario" \
  -e DB_PASSWORD="senha" \
  refugio-api
```

---

## Endpoints

Base: `http://localhost:8080`

| Recurso | Método | Caminho | Status |
|---------|--------|---------|--------|
| Cidadão | GET | `/api/cidadaos` | 200 |
| Cidadão | GET | `/api/cidadaos/{id}` | 200 / 404 |
| Cidadão | POST | `/api/cidadaos` | 201 |
| Cidadão | PUT | `/api/cidadaos/{id}` | 200 / 404 |
| Cidadão | DELETE | `/api/cidadaos/{id}` | 204 / 404 |
| Ponto de Refresco | GET/GET{id}/POST/PUT/DELETE | `/api/pontos-refresco` | 200/201/204/404 |
| Alerta | GET/GET{id}/POST/PUT/DELETE | `/api/alertas` | 200/201/204/404 |
| Check-in | GET/GET{id}/POST/PUT/DELETE | `/api/checkins` | 200/201/204/404 |
| Check-in — responder "Tô bem" | POST | `/api/checkins/{id}/responder` | 200 |
| Check-in — escalar um | POST | `/api/checkins/{id}/escalar` | 200 |
| Check-in — escalar todos | POST | `/api/checkins/escalar` | 200 |
| Check-in — cidadãos em risco | GET | `/api/checkins/cidadaos-em-risco` | 200 |

Erros retornam JSON padronizado: `{timestamp, status, error, message, path}`.

### Exemplos (curl)

```bash
curl http://localhost:8080/api/cidadaos

curl -X POST http://localhost:8080/api/cidadaos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Fulana de Tal","dataNascimento":"1950-01-01","telefone":"(11)90000-0000","tipo":"IDOSO","idBairro":1}'

curl -X POST http://localhost:8080/api/checkins/escalar

curl http://localhost:8080/api/checkins/cidadaos-em-risco
```

---

## Stack

- Java 21 · Spring Boot 3.3.5 · `spring-boot-starter-web`
- Oracle `com.oracle.database.jdbc:ojdbc11` 23.5.0.24.07 (JDBC puro)
- Build: Maven Wrapper · Deploy: Docker no render.com
