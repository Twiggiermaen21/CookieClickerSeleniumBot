# Etap 1: Budowanie projektu
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Etap 2: Uruchomienie aplikacji
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Kopiuje zbudowany plik JAR (zmień nazwę, jeśli Twój ma inną)
COPY --from=build /app/target/*-shaded.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]