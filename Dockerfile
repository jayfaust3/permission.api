FROM gradle:7.5.1-jdk17-alpine as builder

COPY build.gradle.kts .
COPY src ./src

RUN gradle clean build --no-daemon

FROM openjdk:17-alpine

EXPOSE 80

COPY --from=builder /home/gradle/build/libs/*.jar /app.jar

CMD [ "java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "/app.jar" ]