# postgres
docker start postgresdb

# kafka
docker start docker-zookeeper-1 docker-kafka2-1 docker-kafka1-1

echo $PWD
# collector
cd $PWD/collector ; bash java-collector.sh

# iglu server
cd $PWD/iglu ; bash java-iglu.sh

# enrich 
cd $PWD/enrich ; bash java-enrich.sh