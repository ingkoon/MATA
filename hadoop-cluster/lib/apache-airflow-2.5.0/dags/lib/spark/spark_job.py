# import findspark
# findspark.init("/usr/local/lib/spark-3.3.2-bin-hadoop3")
#
# # 필요한 라이브러리 import
# from pyspark.sql import SparkSession
# from pyspark.sql.functions import col, count, sum
#
# cassandra_keyspace = "tagmanager"
# cassandra_table = "stream"
#
# # SparkSession 생성
# spark = SparkSession.builder \
#                     .appName("Cassandra to Hive") \
#                     .master("yarn") \
#                     .config("spark.sql.catalogImplementation","hive") \
#                     .enableHiveSupport() \
#                     .getOrCreate()
#
# # Cassandra 데이터 읽기
# df = spark.read \
#          .format("org.apache.spark.sql.cassandra") \
#          .option("spark.cassandra.connection.host", "master01") \
#          .option("spark.cassandra.connection.port", 9042) \
#          .options(table=cassandra_table, keyspace= cassandra_keyspace) \
#          .load()
#
# # 데이터 집계
# agg_df = df.groupBy("position_x", "location") \
#            .agg(count("*").alias("count"), sum("bytes").alias("total_bytes"))
#
#
# # Hive 테이블 생성
# spark.sql("CREATE TABLE IF NOT EXISTS web_logs_agg(position_x STRING, location STRING, position_x_count BIGINT, total_bytes BIGINT) \
#            USING hive")
#
# # Hive에 데이터 저장
# agg_df.write \
#       .mode("append") \
#       .saveAsTable("web_logs_agg")


import findspark
findspark.init("/usr/local/lib/spark-3.3.2-bin-hadoop3")

from pyspark.sql.functions import *
from pyspark.sql.types import *
from pyspark.sql import *


def read_cassandra_to_spark():
    # Cassandra connection details
    cassandra_keyspace = 'tagmanager'
    cassandra_table = 'stream'

    # Spark session
    spark = SparkSession.builder.appName('CassandraToHive').getOrCreate()

    # Create Cassandra dataframe
    cassandra_df = spark.read.format("org.apache.spark.sql.cassandra") \
                    .option("spark.cassandra.connection.host", "master01") \
                    .option("spark.cassandra.connection.port", 9042) \
                    .option("keyspace", cassandra_keyspace) \
                    .option("table", cassandra_table) \
                    .load()

    # Create Hive schema
    schema = StructType([
        StructField("id", IntegerType(), True),
        StructField("name", StringType(), True)
    ])

    # Create Hive table
    hive_table = 'hive_table'
    cassandra_df.write.format("hive").option("table", hive_table).option("schema", "default").save()