 /usr/local/spark/bin/spark-submit --master yarn  --deploy-mode client --num-executors 2 --executor-memory 1g --executor-cores 1 --conf spark.streaming.kafka.allowNonConsecutiveOffsets=true --class org.hust.job.JobsManager target/loader-kafka-1.0-SNAPSHOT.jar --duration $1 --groupId $2 --topics $3