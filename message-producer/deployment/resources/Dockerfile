FROM adoptopenjdk:openj9

RUN mkdir /opt/app
COPY message-producer-0.0.1-SNAPSHOT.jar  /opt/app
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/message-producer-0.0.1-SNAPSHOT.jar"]