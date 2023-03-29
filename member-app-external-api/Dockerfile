# OpenJDK 17 Alpine image
FROM openjdk:17-alpine

# 컨테이너 내부의 /app 디렉토리 생성
WORKDIR /app

# 컨테이너 내부의 /app 디렉토리에 jar 파일 복사
ARG JAR_FILE=build/libs/*.jar
ARG CONTAINER_JAR_FILE=member-app-external-api.jar
COPY ${JAR_FILE} ${CONTAINER_JAR_FILE}

# Spring Boot를 실행하기 위한 entry point 지정
ENTRYPOINT ["java", "-jar", "/app/member-app-external-api.jar"]