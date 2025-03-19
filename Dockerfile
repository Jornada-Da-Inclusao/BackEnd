FROM openjdk:17.0.1-jdk-oracle as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod -R 777 ./mvnw

RUN ./mvnw install -DskipTests

# Verifique se o arquivo .jar foi criado corretamente
RUN ls /workspace/app/target/*.jar

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Verifique se os arquivos foram extraídos corretamente
RUN ls -R /workspace/app/target/dependency/BOOT-INF

FROM openjdk:17.0.1-jdk-oracle

VOLUME /tmp

ARG DEPENDENCY=/workspace/app/target/dependency

# Verifique se os arquivos de dependências estão presentes
RUN ls -R ${DEPENDENCY}

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.fatec.IntegraKidsApplication"]
