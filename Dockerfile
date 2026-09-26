FROM eclipse-temurin:21-jre

RUN groupadd --system nonroot \
    && useradd --system --gid nonroot nonroot

USER nonroot

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]