# Etapa 1: Build (Compilação do código)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Run (Execução da aplicação)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia o jar gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que o Spring Boot usa
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]