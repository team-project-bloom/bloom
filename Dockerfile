# Stage 1: Build the app with Maven
FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /application
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Dcheckstyle.skip

# Stage 2: Extract Spring Boot layers
FROM openjdk:17-jdk-slim AS builder
WORKDIR /application
ARG JAR_FILE=target/*.jar
COPY --from=build /application/target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Stage 3: Final image
FROM openjdk:17-jdk-slim
WORKDIR /application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
