FROM maven:3.8.3-openjdk-17-slim as builder

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:17.0.1-jdk-slim

COPY --from=builder /usr/src/app/target/*.jar /usr/app/pectus.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/usr/app/pectus.jar"]