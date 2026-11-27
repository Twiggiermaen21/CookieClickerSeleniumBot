FROM maven:3.9-eclipse-temurin-25
RUN apt-get update && apt-get install -y chromium-driver && rm -rf /var/lib/apt/lists/*
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn test-compile -B

ENTRYPOINT ["mvn", "test", "-B"]