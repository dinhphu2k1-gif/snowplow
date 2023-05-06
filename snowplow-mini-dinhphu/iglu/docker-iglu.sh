docker run \
  -v $PWD/config.hocon:/iglu/config.hocon \
  -p 8181:8181 \
  --name iglu-server \
  snowplow/iglu-server:0.9.1 --config /iglu/config.hocon