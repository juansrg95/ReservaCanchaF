# ===== Etapa 1: Build con Gradle (Java 21) =====
FROM gradle:8.8-jdk21-alpine AS builder
WORKDIR /workspace

# Copiamos lo mínimo para cachear dependencias
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle/

# FIX: convertir CRLF a LF y dar permisos de ejecución
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Descargamos dependencias (cachea)
RUN ./gradlew dependencies --no-daemon || true

# Copiamos el resto del código
COPY . .

# Construimos el jar ejecutable
RUN ./gradlew clean bootJar --no-daemon

# ===== Etapa 2: Runtime mínimo (JRE 21) =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el JAR desde el builder
COPY --from=builder /workspace/build/libs/*.jar app.jar

# Puerto de Spring Boot
EXPOSE 8080

# Variables opcionales (se pueden sobrescribir con -e)
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

