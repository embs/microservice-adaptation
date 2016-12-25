FROM openjdk:8-jdk-alpine

RUN mkdir -p /home/app/repository

COPY ../target/ReqResp.jar /home/app
COPY ../target/ResponseTime.jar /home/app
VOLUME /home/app/src/main/resources

WORKDIR /home/app
EXPOSE 5560

ENTRYPOINT ["java", "-jar"]
