FROM adalrsjr/spot-alpine

RUN mkdir -p /home/app

#COPY target/ReqResp.jar /home/app
#COPY target/ResponseTime.jar /home/app
COPY target/processor-unit-0.0.1-SNAPSHOT-jar-with-dependencies.jar /home/app
COPY src/main/resources/libs/jhoafparser-1.1.1.jar /home/app
COPY docker/processing-unit.properties /home/app
COPY src/main/resources/log4j2.xml /home/app
VOLUME /home/app/src/main/resources
WORKDIR /home/app
EXPOSE 5560

ENTRYPOINT ["java", "-cp", "."]
