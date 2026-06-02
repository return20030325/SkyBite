# 使用OpenJDK官方镜像
FROM openjdk:8-jre-alpine
# 或者使用较新版本
# FROM openjdk:11-jre-slim


WORKDIR /app


COPY ./app.jar app.jar


EXPOSE 8080


ENV JAVA_OPTS=""


ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]