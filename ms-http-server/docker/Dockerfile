FROM openjdk:8-jre-alpine

RUN mkdir -p /home/app/

#COPY target/server.jar /home/app
COPY target/ms-http-server-0.0.1-SNAPSHOT-jar-with-dependencies.jar /home/app
COPY src/main/resources/log4j2.xml /home/app

WORKDIR /home/app

EXPOSE 8080

#docker run -it --rm --name adaptation --net alpine_default -v C:\Users\adalr\Development\java\workspace\adaptionmanager\scripts:/home/app/scripts maveric/adaptation
ENTRYPOINT ["java", "-jar","/home/app/ms-http-server-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
CMD ["java", "-jar","/home/app/ms-http-server-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
