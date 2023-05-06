java -jar snowplow-enrich-kafka-3.8.0.jar \
  --enrichments ./enrichments \
  --iglu-config ./resolver.json \
  --config ./config.hocon