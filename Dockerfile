# ESTÁGIO 1: Build
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila o projeto e pula os testes (para ser mais rápido no build)
RUN mvn clean package -DskipTests

# ESTÁGIO 2: Runtime
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
# Copia apenas o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]