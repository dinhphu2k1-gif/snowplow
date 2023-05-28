FROM ubuntu:20.04

WORKDIR /root

RUN apt-get update && apt-get install -y openjdk-8-jdk wget git 

# clone snowplow open source
RUN git clone https://github.com/dinhphu2k1-gif/snowplow.git

# clone web
RUN git clone https://github.com/dinhphu2k1-gif/book-shop-web.git

# set up env variable
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 
ENV PATH=$PATH:$JAVA_HOME/bin

#set up docker
RUN apt install apt-transport-https ca-certificates curl software-properties-common && \
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add - && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable" && \
    apt install docker-ce

# set up postgres db
RUN docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=snowplow -d postgres

# set up mongo db
RUN docker run --name mongodb -p 27017:27017 -d mongo

# set up kafka
RUN cd /root/snowplow/snowplow-mini-dinhphu/kafka/docker && \
    docker compose up

# set up elasticsearch
RUN cd /root/snowplow/snowplow-mini-dinhphu/elasticsearch/docker && \
    sysctl -w vm.max_map_count=262144 && \
    docker compose up



# export port
# port web book
EXPOSE 3000
# port admin
EXPOSE 3001






