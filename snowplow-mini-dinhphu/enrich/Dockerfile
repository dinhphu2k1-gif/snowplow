FROM ubuntu:20.04

# install java
RUN apt-get update && apt-get install -y  openjdk-11-jdk

WORKDIR /enrich

ADD . /enrich

ENTRYPOINT [ "bash", "java-enrich.sh" ]