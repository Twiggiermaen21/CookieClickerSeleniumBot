FROM maven:3.9-eclipse-temurin-25
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn test-compile -B

ENTRYPOINT ["mvn", "test", "-B"]