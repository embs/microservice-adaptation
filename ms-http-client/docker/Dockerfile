FROM openjdk:8-jdk-alpine

RUN mkdir -p /home/app/

COPY target/client.jar /home/app
COPY src/main/resources/log4j2.xml /home/app

WORKDIR /home/app

#docker run -it --rm --name adaptation --net alpine_default -v C:\Users\adalr\Development\java\workspace\adaptionmanager\scripts:/home/app/scripts maveric/adaptation
ENTRYPOINT ["java", "-Dserver", "-Dthrottle","-jar","/home/app/client.jar"]
CMD ["java", "-Dserver", "-Dthrottle","-jar","/home/app/client.jar"]
