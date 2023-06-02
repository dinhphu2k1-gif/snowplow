FROM ubuntu:20.04

# install java
RUN apt-get update && apt-get install -y  openjdk-8-jdk

WORKDIR /collector

ADD . /collector

ENTRYPOINT [ "bash", "java-collector.sh" ]

EXPOSE 8080