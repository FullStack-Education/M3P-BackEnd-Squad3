FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Execução
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/labpcp-0.0.1-SNAPSHOT.jar /app/labpcp.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/labpcp.jar"]
