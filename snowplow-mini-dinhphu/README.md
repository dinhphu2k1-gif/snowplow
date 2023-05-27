# Cài đặt snowplow mini với Kafka bằng Docker

## 1. Prerequisite
Trước khi bắt đầu cần có Postgresql và Kafka trước

***Postgres***
```
docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```

***Kafka cluster*** (2 broker và 1 zookeeper)
chạy file `docker-compose.yaml` (chạy như nào thì ae tự search gg nhé)trong folder [`kafka/docker`](./kafka/docker/)


**Note:** các lần chạy phía sau chỉ cần start container là được rồi

## 2. Component
Chạy các component theo đúng thứ tự nhé (`iglu` cần chạy trước `enrich`)

### 2.1. [Collector](https://docs.snowplow.io/docs/pipeline-components-and-applications/stream-collector/setup/) 

Start collector ```bash collector/java-collector.sh```
Server chạy ở port `8080`

### 2.2. [Iglu](https://docs.snowplow.io/docs/pipeline-components-and-applications/iglu/iglu-repositories/iglu-server/setup/)

Start iglu ```bash collector/java-iglu.sh```
Server chạy ở port `8081`

### 2.2. [Enrich](https://docs.snowplow.io/docs/pipeline-components-and-applications/enrichment-components/enrich-kafka/)

Start enrich ```bash collector/java-iglu.sh```

## 3. Storage

### 3.1. Elasticsearch
Các sự kiện được sử dụng sẽ được lưu trong elasticsearch để thuận tiện trong việc truy vấn. Chủ yếu là query theo `user_id`
