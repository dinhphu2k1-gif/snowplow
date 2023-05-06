docker run \
  -v $PWD/config:/snowplow/config \
  snowplow/snowplow-postgres-loader:0.3.3 \
  --resolver /snowplow/config/resolver.json \
  --config /snowplow/config/config.hocon