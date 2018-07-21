FROM openjdk:8-jdk-alpine
COPY ./target/data-pipeline-1.0.0.jar /usr/src/app/
WORKDIR /usr/src/app
EXPOSE 8088
CMD ["java", "-jar", "data-pipeline-1.0.0.jar"]