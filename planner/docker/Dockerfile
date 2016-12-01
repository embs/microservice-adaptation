FROM openjdk:8-jdk-alpine

RUN mkdir -p /home/app/repository

COPY ../target/planner.jar /home/app
VOLUME ["/home/app/repository"]

WORKDIR /home/app
EXPOSE 5560

ENTRYPOINT ["java", "-jar", "/home/app/planner.jar"]
