FROM adalrsjr/spot-alpine

RUN mkdir -p /home/app

COPY target/monolith-0.0.1-SNAPSHOT-jar-with-dependencies.jar /home/app
COPY src/main/resources/libs/jhoafparser-1.1.1.jar /home/app
COPY docker/processing-unit.properties /home/app
COPY docker/adaptation.properties /home/app
COPY docker/planner.properties /home/app
COPY src/main/resources/log4j2.xml /home/app
COPY scripts /home/app/scripts
COPY repository/policies.json /home/app/repository/

VOLUME /home/app/src/main/resources
WORKDIR /home/app
EXPOSE 5560

ENTRYPOINT ["java", "-cp", ".:jhafparser-1.1.1.jar" "-jar", "monolith-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
