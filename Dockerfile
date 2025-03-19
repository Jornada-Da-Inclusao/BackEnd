FROM openjdk:17.0.1-jdk-oracle as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod -R 777 ./mvnw

RUN ./mvnw install -DskipTests

# Verifique se o JAR foi criado
RUN ls /workspace/app/target/IntegraKids-0.0.1-SNAPSHOT.jar

# Criação do diretório para dependências
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../IntegraKids-0.0.1-SNAPSHOT.jar)

# Verifique se a extração do JAR foi bem-sucedida
RUN ls -R /workspace/app/target/dependency/BOOT-INF

FROM openjdk:17.0.1-jdk-oracle

VOLUME /tmp

ARG DEPENDENCY=/workspace/app/target/dependency

# Verifique se as dependências estão sendo copiadas corretamente
RUN ls -R ${DEPENDENCY}

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.fatec.IntegraKidsApplication"]
