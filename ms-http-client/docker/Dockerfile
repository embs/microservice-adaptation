FROM openjdk:8-jre-alpine

RUN mkdir -p /home/app/

#COPY target/client.jar /home/app
COPY target/ms-http-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar /home/app
COPY src/main/resources/log4j2.xml /home/app

WORKDIR /home/app

#docker run -it --rm --name adaptation --net alpine_default -v C:\Users\adalr\Development\java\workspace\adaptionmanager\scripts:/home/app/scripts maveric/adaptation
ENTRYPOINT ["java", "-Dserver", "-Dthrottle","-jar","/home/app/ms-http-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
CMD ["java", "-Dserver", "-Dthrottle","-jar","/home/app/ms-http-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
