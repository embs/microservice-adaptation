FROM openjdk:8-jdk-alpine

RUN mkdir -p /home/app/scripts

COPY target/AdaptationManager.jar /home/app
COPY src/main/resources/log4j2.xml /home/app
COPY docker/adaptation.properties /home/app
VOLUME /home/app/src/main/resources/logs
WORKDIR /home/app

#docker run -it --rm --name adaptation --net alpine_default -v C:\Users\adalr\Development\java\workspace\adaptionmanager\scripts:/home/app/scripts maveric/adaptation
ENTRYPOINT ["java","-jar","/home/app/AdaptationManager.jar"]
CMD ["java", "-jar", "/home/app/AdaptationManager.jar"]
