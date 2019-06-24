FROM openjdk:12.0.1-oracle

WORKDIR /app

COPY ./build/libs/cojesc-*.jar ./cojesc.jar
COPY ./docker/files ./

CMD ["sh", "/app/init.sh", "java", "-jar", "./cojesc.jar"]
