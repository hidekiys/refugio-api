# Registro de Uso de Inteligência Artificial — Projeto Refúgio

**Global Solution 2026.2 — CitizenTech / FIAP**
**Equipe Refúgio** · Disciplinas: *Domain Driven Design (Java)* e *Database Design*

| Integrante | RM |
|---|---|
| Artur Rodrigues | RM564309 |
| Daniel Duarte | RM562508 |
| Felippe Nascimento Silva | RM562123 |
| Kawan Oliveira Amorim | RM562197 |
| Matheus Hideki Doroszewski Yoshimura | RM564970 |

---

## 1. Ferramenta utilizada

- **Assistente:** Claude (Anthropic), via **Claude Code**.
- **Papel atribuído à IA:** atuar como *tech lead* de apoio — ajudar no planejamento, na modelagem de dados, na geração e revisão de código e na documentação, **sempre sob decisão e validação da equipe**.
- **Período:** fase de planejamento até a entrega final (19/11/2026).

> **Transparência:** a IA foi usada como ferramenta de produtividade e consultoria técnica. Nenhuma entrega foi aceita sem revisão humana. Todo o SQL foi **executado e validado em um Oracle Database real** antes de ser considerado pronto, e o código Java foi compilado, executado e testado pela equipe.

---

## 2. Como a IA foi utilizada, por área

### 2.1 Planejamento e arquitetura
- Leitura e análise dos PDFs de requisitos da GS.
- Definição da arquitetura em camadas (model / dao / bo / controller / exception / config) com toques de DDD (agregados, *value objects* `Coordenada` e `IndiceDeCalor`).
- Registro de todas as decisões técnicas em um documento de decisões do projeto.

### 2.2 Database Design
- Apoio na modelagem das **9 tabelas** (BAIRRO, CIDADAO, CUIDADOR, AGENTE_SAUDE, PONTO_REFRESCO, HORARIO_FUNC, LEITURA_CLIMA, ALERTA, CHECKIN) e do DER.
- Geração dos scripts `ddl_refugio.sql`, `dml_refugio.sql` e `dql_refugio.sql`.
- **Validação:** todos os scripts foram rodados pela equipe em um **Oracle XE 21c local** e ajustados até executarem sem erro.

### 2.3 Domain Driven Design (Java)
- Geração do esqueleto do projeto Spring Boot (Maven) com **JDBC puro** (sem JPA/Hibernate), decisão tomada pela equipe.
- Implementação das entidades, DAOs, regras de negócio (incluindo a **escalada do check-in "Tô bem"**) e dos controllers REST.
- Apoio na resolução de problemas de ambiente (configuração da JDK 21 na IDE, Maven Wrapper, driver Oracle).

### 2.4 Documentação
- Redação dos documentos técnicos (HTML para exportação em PDF), com diagramas.
- Preenchimento das fichas/capas com integrantes, RMs e links.

---

## 3. Prompts representativos (reais)

Abaixo, exemplos reais de comandos dados à IA ao longo do projeto (resumidos):

1. *"Analise toda a documentação e vamos planejar por onde começar — você é o tech lead."*
2. *"Ficaremos responsáveis também pelo banco de dados (database)."*
3. *"Vamos usar JDBC puro."*
4. *"Começar modelagem."* → *"Gerar DDL"* → *"Gerar DML"* → *"Gerar DQL."*
5. *"Podemos fazer local enquanto não temos a credencial? Eu já tenho Oracle XE nativo."*
6. *"Acredito que já tenho o JDK 21 — dá pra fazer com Spring?"*
7. *"No IntelliJ: Cannot resolve symbol 'SpringBootApplication'."* (troubleshooting de ambiente)
8. *"Gerar o modelo lógico."*
9. *"O que falta para entregarmos?"*

---

## 4. Validação e responsabilidade da equipe

- **SQL:** executado em Oracle real (XE 21c) — DDL, DML e as 3 consultas DQL exigidas foram verificadas uma a uma.
- **Java:** o projeto foi importado na IDE, compilado com JDK 21 e **executado pela equipe**; a camada de acesso a dados foi testada contra o banco.
- **Revisão:** cada artefato gerado pela IA foi lido, entendido e ajustado pela equipe antes de entrar no projeto.
- A responsabilidade técnica e autoral pelas entregas é **da equipe**; a IA foi instrumento de apoio.

---

## 5. Reflexão crítica

O uso da IA acelerou tarefas repetitivas (boilerplate de DAOs, scripts SQL, estrutura de documentação) e ajudou a destravar problemas de configuração de ambiente. O ganho principal foi de **velocidade e organização**, não de substituição do raciocínio: as decisões de arquitetura, modelagem e regras de negócio foram discutidas e aprovadas pela equipe, e nada foi entregue sem ser testado e compreendido.
