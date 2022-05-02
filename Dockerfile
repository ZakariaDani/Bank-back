FROM openjdk:8-alpine
VOLUME /tmp
ADD target/*.jar /bank.jar
ENTRYPOINT ["java","-jar","/bank.jar"]
EXPOSE 8080