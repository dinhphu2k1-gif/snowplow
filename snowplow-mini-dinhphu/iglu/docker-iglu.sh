docker run \
  -v ./config.hocon:/iglu/config.hocon \
  -p 8181:8181 \
  --name iglu-server \
  snowplow/iglu-server --config /iglu/config.hocon