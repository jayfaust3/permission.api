FROM gradle:7.5.1-jdk11-alpine as builder
COPY build.gradle.kts .
COPY src ./src
RUN gradle clean build --no-daemon
FROM openjdk:11-jre-slim
EXPOSE 80
COPY --from=builder /home/gradle/build/libs/*.jar /app.jar
CMD [ "java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "/app.jar" ]