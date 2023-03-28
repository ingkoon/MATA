import findspark
findspark.init("/usr/local/lib/spark-3.3.2-bin-hadoop3")

from pyspark import SparkConf
from pyspark.sql.functions import *
from pyspark.sql.types import *
from pyspark.sql import *
from pyspark.sql.functions import udf, col, from_json, pandas_udf, split

def streaming_job():
    session = SparkSession.builder \
        .appName("Streaming_Kafka_To_Cassandra") \
        .master("yarn") \
        .config("spark.yarn.queue", "stream") \
        .config("spark.jars.packages", "org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.2,com.datastax.spark:spark-cassandra-connector_2.12:3.3.0") \
        .getOrCreate()
    
    
    kafka_bootstrap_servers = 'master01:9092,master02:9092,slave01:9092,slave02:9092,slave03:9092'
    topic = 'tagmanager'
    cassandra_keyspace = "tagmanager"
    cassandra_table = "stream"
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
                    StructField("prevLocation", StringType()),
                    StructField("referrer", StringType()),
                    StructField("timestamp", LongType()),
                    StructField("pageDuration", LongType())
            ]
    )

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
    
    streaming_query = streaming_df.select("key", "value.*") \
        .withColumnRenamed("serviceToken", "service_token") \
        .withColumnRenamed("clientId", "client_id") \
        .withColumnRenamed("serviceId", "service_id") \
        .withColumnRenamed("sessionId", "session_id") \
        .withColumnRenamed("event", "event") \
        .withColumnRenamed("targetId", "target_id") \
        .withColumnRenamed("positionX", "position_x") \
        .withColumnRenamed("positionY", "position_y") \
        .withColumnRenamed("location", "location") \
        .withColumnRenamed("prevLocation", "prev_location") \
        .withColumnRenamed("referrer", "referrer") \
        .withColumnRenamed("timestamp", "creation_timestamp") \
        .withColumnRenamed("pageDuration", "page_duration")
    
    query = streaming_query.writeStream.outputMode("append") \
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
    query.awaitTermination()
    
    session.stop()