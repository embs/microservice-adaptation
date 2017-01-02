FROM openjdk:8-jdk-alpine

RUN mkdir -p /home/app/repository

#COPY target/Planner.jar /home/app
COPY target/planner-0.0.1-SNAPSHOT-jar-with-dependencies.jar /home/app
COPY repository/policies.json /home/app/repository
COPY src/main/resources/log4j2.xml /home/app
COPY docker/planner.properties /home/app
VOLUME /home/app/src/main/resources

WORKDIR /home/app
EXPOSE 5560

ENTRYPOINT ["java", "-jar", "/home/app/planner-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
