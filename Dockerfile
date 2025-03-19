# Etapa de build
FROM openjdk:17.0.1-jdk-oracle as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod -R 777 ./mvnw

RUN ./mvnw install -DskipTests

# Verificando o conteúdo do diretório target
RUN ls -alh target/

# Extração do JAR gerado
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf $(ls ../target/*.jar | head -n 1))

# Etapa final
FROM openjdk:17.0.1-jdk-oracle

WORKDIR /app

# Copiando o conteúdo do build
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Usando o comando padrão para Spring Boot
ENTRYPOINT ["java", "-jar", "/app/IntegraKids-0.0.1-SNAPSHOT.jar"]
