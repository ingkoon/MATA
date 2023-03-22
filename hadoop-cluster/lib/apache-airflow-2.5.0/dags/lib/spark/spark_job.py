# 필요한 라이브러리 import
from pyspark.sql import SparkSession
from pyspark.sql.functions import col, count, sum

# SparkSession 생성
spark = SparkSession.builder \
                    .appName("Cassandra to Hive") \
                    .config("spark.sql.catalogImplementation","hive") \
                    .enableHiveSupport() \
                    .getOrCreate()

# Cassandra 데이터 읽기
df = spark.read \
         .format("org.apache.spark.sql.cassandra") \
         .options(table="web_logs", keyspace="my_keyspace") \
         .load()

# 데이터 집계
agg_df = df.groupBy("date", "url") \
           .agg(count("*").alias("count"), sum("bytes").alias("total_bytes"))

# Hive에 데이터 저장
agg_df.write \
      .mode("overwrite") \
      .saveAsTable("web_logs_agg")