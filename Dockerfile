FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

RUN chmod +x ./gradlew && ./gradlew bootJar -x test

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

