FROM openjdk:8-jdk-alpine

RUN apk --no-cache --update add tzdata openntpd
RUN cp /usr/share/zoneinfo/America/Recife /etc/localtime && echo "America/Recife" > /etc/timezone
RUN mkdir -p /home/app/scripts

COPY target/AdaptationManager.jar /home/app
COPY src/main/resources/log4j2.xml /home/app
COPY docker/adaptation.properties /home/app

WORKDIR /home/app

#docker run -it --rm --name adaptation --net alpine_default -v C:\Users\adalr\Development\java\workspace\adaptionmanager\scripts:/home/app/scripts maveric/adaptation
ENTRYPOINT ["java","-jar","/home/app/AdaptationManager.jar"]
CMD ["java", "-jar", "/home/app/AdaptationManager.jar"]
