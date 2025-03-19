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

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.fatec.IntegraKidsApplication"]
