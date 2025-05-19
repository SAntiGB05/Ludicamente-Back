# Imagen base de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR al contenedor
COPY target/Ludicamente-0.0.1-SNAPSHOT.jar app.jar

# Expón el puerto (ajusta si usas otro)
EXPOSE 8080

# Comando para ejecutar tu aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
