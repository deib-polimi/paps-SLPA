FROM maven:3.8.1-openjdk-16-slim AS build
RUN mkdir /build
COPY . /build
WORKDIR /build
RUN mvn clean package --threads 8 -DskipTests

FROM openjdk:16-jdk-slim
RUN mkdir /app
LABEL name="SLPA Microservice"
COPY --from=build /build/target/paps-SLPA-rest-jar-with-dependencies.jar /app/paps-SLPA-rest-jar-with-dependencies.jar
WORKDIR /app
CMD "java" "-jar" "paps-SLPA-rest-jar-with-dependencies.jar"
