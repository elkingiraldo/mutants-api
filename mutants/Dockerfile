FROM openjdk:8-jdk-alpine

MAINTAINER elkin.giraldo.pinedo@gmail.com
VOLUME /tmp
EXPOSE 8080

ADD target/mutants-0.0.1-SNAPSHOT.jar mutants.jar

ENV WAIT_VERSION 2.7.3
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

CMD /wait && java -jar mutants.jar