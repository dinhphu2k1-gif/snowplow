java -jar snowplow-enrich-kafka-3.8.0.jar \
  --enrichments ../enrichments \
  --iglu-config iglu_resolver.json \
  --config config.minimal.hocon