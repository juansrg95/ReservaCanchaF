# ===== Etapa 1: Build con Gradle (Java 21) =====
FROM gradle:8.8-jdk21-alpine AS builder
WORKDIR /workspace

# Copiamos lo mínimo para cachear dependencias
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle/

# Arreglamos EOL y permisos para gradlew
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Descargamos dependencias (capa caché)
RUN ./gradlew dependencies --no-daemon || true

# Ahora copiamos TODO el código (esto pisa gradlew otra vez)
COPY . .

# VOLVEMOS a arreglar gradlew porque COPY . . lo sobrescribe
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Construimos el jar
RUN ./gradlew clean bootJar --no-daemon

# ===== Etapa 2: Runtime mínimo (JRE 21) =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el jar construido
COPY --from=builder /workspace/build/libs/*.jar app.jar

EXPOSE 8080
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


