# 자바 버전
FROM amazoncorretto:17-alpine

ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-jar", "app.jar"]
