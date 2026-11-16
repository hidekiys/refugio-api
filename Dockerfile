# =====================================================================
# Refugio API - Dockerfile multi-stage para deploy no render.com
# =====================================================================

# ---------- Estagio 1: build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Aproveita cache de dependencias: copia o pom e baixa as dependencias antes.
# (|| true: go-offline as vezes falha ao resolver plugins; nao deve quebrar o build.)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline || true

# Copia o codigo e empacota o jar (sem rodar testes).
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Estagio 2: runtime ----------
FROM eclipse-temurin:21-jre AS run
WORKDIR /app

# Copia o jar gerado no estagio de build.
COPY --from=build /app/target/*.jar app.jar

# O render.com injeta a porta via variavel de ambiente PORT (default 8080).
ENV PORT=8080
EXPOSE 8080

# As credenciais do Oracle (DB_URL, DB_USERNAME, DB_PASSWORD) sao injetadas
# como variaveis de ambiente no painel do render.
ENTRYPOINT ["java", "-jar", "app.jar"]
