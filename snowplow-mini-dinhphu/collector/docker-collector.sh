echo $PWD

docker run \
  -v $PWD/config.hocon:/snowplow/config.hocon \
  -p 8080:8080 \
  --name collector-kafka \
  snowplow/scala-stream-collector-kafka:2.9.0 \
  --config /snowplow/config.hocon