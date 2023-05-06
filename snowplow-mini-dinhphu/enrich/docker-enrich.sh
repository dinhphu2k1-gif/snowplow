docker run \
  -it \
  -v $PWD:/snowplow \
  --name enrich-kafka \
  snowplow/snowplow-enrich-kafka:3.8.0 \
  --enrichments /snowplow/enrichments \
  --iglu-config /snowplow/resolver.json \
  --config /snowplow/config.hocon