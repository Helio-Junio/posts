# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte
COPY src ./src

# Compila o projeto
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR compilado do stage anterior
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta
EXPOSE 8080

# Define variáveis de ambiente padrão
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para iniciar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]