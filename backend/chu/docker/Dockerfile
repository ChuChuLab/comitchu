# Java 17 베이스 이미지 사용
FROM openjdk:17-jre

# Dockerfile 내에서 사용할 변수 JAR_FILE을 정의
ARG JAR_FILE=*.jar

COPY ${JAR_FILE} spring-prod.jar

ENTRYPOINT ["java", "-jar", "/spring-prod.jar"]