FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY . .
RUN chmod +x gradlew && ./gradlew clean bootJar -x test

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/build/libs/productjpa-0.0.1-SNAPSHOT.jar /app/app.jar
COPY render-start.sh /app/render-start.sh
RUN chmod +x /app/render-start.sh

EXPOSE 8080

CMD ["/app/render-start.sh"]
