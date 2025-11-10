FROM eclipse-temurin:17-jdk

# Dockerfile 내에서 사용할 변수 JAR_FILE을 정의
ARG JAR_FILE=*.jar

COPY ${JAR_FILE} spring-prod.jar

ENTRYPOINT ["java", "-jar", "/spring-prod.jar"]
