FROM maven:3.9.5 AS build
COPY /petru .
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
COPY --from=build /petru/target/petru-0.0.1-SNAPSHOT.jar petru.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "petru.jar"]