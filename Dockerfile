FROM eclipse-temurin:17-jdk

# 애플리케이션 JAR 파일을 컨테이너로 복사
COPY build/libs/*SNAPSHOT.jar /app.jar

# 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "/app.jar"]

#ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-jar", "/app.jar"]