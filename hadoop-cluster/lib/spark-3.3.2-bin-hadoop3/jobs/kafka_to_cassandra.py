from pyspark import SparkConf
from pyspark.sql.functions import *
from pyspark.sql.types import *
from pyspark.sql import *

# spark-cassandra-connector 아카이브 파일을 Spark Driver에 제출
sconf = SparkConf()
sconf.setAppName("KafkaToCassandraStream").set("spark.jars.packages", "org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.2,com.datastax.spark:spark-cassandra-connector_2.12:3.3.0")

# Spark Context 생성
sc = SparkContext(conf=sconf)

# Kafka Broker, Topic 정의
kafka_bootstrap_servers = 'master01:9092,master02:9092,slave01:9092,slave02:9092,slave03:9092'
topic = 'tagmanager'

# Kafka Producer record의 value 필드에 직렬화 되어있는 JSON 객체의 스키마 정
schema = StructType(
        [
                StructField("serviceToken", StringType()),
                StructField("clientId", LongType()),
                StructField("serviceId", LongType()),
                StructField("sessionId", StringType()),
                StructField("event", StringType()),
                StructField("targetId", StringType()),
                StructField("positionX", IntegerType()),
                StructField("positionY", IntegerType()),
                StructField("location", StringType()),
                StructField("timestamp", LongType())
        ]
)

# Spark Session 생성
session = SparkSession(sc)

#Spark Session의 Kafka Source 설정
streaming_df = session \
  .readStream \
  .format("kafka") \
  .option("kafka.bootstrap.servers", kafka_bootstrap_servers) \
  .option("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer") \
  .option("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer") \
  .option("failOnDataLoss","False") \
  .option("subscribe", topic) \
  .load() \
  .withColumn("key", col("key").cast("string")) \
  .withColumn("value", from_json(col("value").cast("string"), schema))
streaming_df.printSchema()

# 전처리(key, value.* 값으로 테이블 생성)
streamming_query = streaming_df.select("key", "value.*") \
    .withColumnRenamed("serviceToken", "service_token") \
    .withColumnRenamed("clientId", "client_id") \
    .withColumnRenamed("serviceId", "service_id") \
    .withColumnRenamed("sessionId", "session_id") \
    .withColumnRenamed("event", "event") \
    .withColumnRenamed("targetId", "target_id") \
    .withColumnRenamed("positionX", "position_x") \
    .withColumnRenamed("positionY", "position_y") \
    .withColumnRenamed("location", "location") \
    .withColumnRenamed("timestamp", "creation_timestamp")

# Cassandra sink 키스페이스, 테이블 명 설정
cassandra_keyspace = "tagmanager"
cassandra_table = "stream"
query = streamming_query.writeStream.outputMode("append") \
      .format("org.apache.spark.sql.cassandra") \
  .option("checkpointLocation", "/") \
  .option("spark.cassandra.connection.host", "master01") \
  .option("spark.cassandra.connection.port", 9042) \
  .option("keyspace", cassandra_keyspace) \
  .option("table", cassandra_table) \
  .option("spark.cassandra.connection.remoteConnectionsPerExecutor", 10) \
  .option("spark.cassandra.output.concurrent.writes", 1000) \
  .option("spark.cassandra.concurrent.reads", 512) \
  .option("spark.cassandra.output.batch.grouping.buffer.size", 1000) \
  .option("spark.cassandra.connection.keep_alive_ms", 600000000) \
      .start()

# 중단될 때 까지 스트리밍
query.awaitTermination()

# 작업 중단 시 Spark Session, Spark Context 종료
session.stop()
sc.stop()