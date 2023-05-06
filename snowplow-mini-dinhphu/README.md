# Cài đặt snowplow mini với Kafka bằng Docker

## 1. Prerequisite
Trước khi bắt đầu cần có Postgresql và Kafka trước

Postgres
```
docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```

Kafka cluster (2 broker và 1 zookeeper)
chạy file docker-compose.yaml trong folder [`kafka/docker`](./kafka/docker/)

