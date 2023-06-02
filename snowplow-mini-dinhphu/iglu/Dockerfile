FROM ubuntu:20.04

# install java
RUN apt-get update && apt-get install -y  openjdk-8-jdk

WORKDIR /iglu

ADD . /iglu

ENTRYPOINT [ "bash", "java-iglu.sh" ]

EXPOSE 8181