# FROM gradle:7.5.1-jdk17-alpine as builder
# COPY build.gradle.kts .
# COPY src ./src
# RUN gradle clean build --no-daemon
# FROM openjdk:17-jdk-slim
# EXPOSE 80
# COPY --from=builder /home/gradle/build/libs/*.jar /app.jar
# CMD [ "java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "/app.jar" ]

FROM openjdk:17-jdk-slim
EXPOSE 80
COPY build/libs/permission.api-0.0.1-SNAPSHOT.jar /app.jar
CMD [ "java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "/app.jar" ]