FROM openjdk:8-jdk-alpine

RUN mkdir -p /home/app/repository

COPY ../target/Planner.jar /home/app
COPY ../repository/policies.json /home/app/repository
VOLUME /home/app/src/main/resources
WORKDIR /home/app
EXPOSE 5560

ENTRYPOINT ["java", "-jar", "/home/app/planner.jar"]
