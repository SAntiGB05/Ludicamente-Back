# Etapa 1: Construcción del proyecto
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Copia el código fuente al contenedor
COPY . /app

WORKDIR /app

# Construye el proyecto (sin correr tests)
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia el .jar desde la etapa anterior
COPY --from=build /app/target/Ludicamente-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]