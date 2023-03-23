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
from datetime import datetime
from datetime import timedelta


def read_cassandra_to_spark():
    # Cassandra connection details
    cassandra_keyspace = 'tagmanager'
    cassandra_table = 'stream'

    # Spark session
    spark = SparkSession.builder \
        .appName("CassandraToHive") \
        .master("yarn") \
        .config("spark.jars.packages",
                "org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.2,com.datastax.spark:spark-cassandra-connector_2.12:3.3.0") \
        .config("spark.hadoop.hive.exec.dynamic.partition.mode", "nonstrict") \
        .enableHiveSupport() \
        .getOrCreate()

    # Create Cassandra dataframe
    cassandra_df = spark.read.format("org.apache.spark.sql.cassandra") \
                    .option("spark.cassandra.connection.host", "master01") \
                    .option("spark.cassandra.connection.port", 9042) \
                    .option("keyspace", cassandra_keyspace) \
                    .option("table", cassandra_table) \
                    .load()

    print(cassandra_df)

    base_time = "2023-03-22 16:00:00"

    # # 해당 시간 사이의 모든 데이터 조회
    # batch_df.select("*") \
    #     .where(col("creation_timestamp") \
    #            .between(*timestamp_range(base_time, 1, 'm'))) \
    #     .show()
    #
    # # 해당 시간 사이에 http://localhost:3000/second에서 일어난 click 이벤트 조회
    # batch_df.select("*") \
    #     .where(col("creation_timestamp") \
    #            .between(*timestamp_range(base_time, 1, 'm'))) \
    #     .where(col("location") \
    #            .like("http://localhost:3000/second")) \
    #     .where(col("event") \
    #            .like("click")) \
    #     .show()
    #
    # # 해당 시간 사이에 http://localhost:3000/second에 접속한 사용자 조회
    # batch_df.select("*") \
    #     .where(col("creation_timestamp") \
    #            .between(*timestamp_range(base_time, -30, 'm'))) \
    #     .where(col("location") \
    #            .like("http://localhost:3000/second")) \
    #     .select("session_id").distinct() \
    #     .show()
    #
    # # location, event 기준으로 그룹핑 후 개수 세기
    # batch_df.select("*") \
    #     .where(col("creation_timestamp") \
    #            .between(*timestamp_range(base_time, 1, 'D'))) \
    #     .groupBy("location", "event").count() \
    #     .show()
    #
    # # session_id 기준으로 해당 시간동안의 체류시간 연산
    # batch_df.select("*") \
    #     .where(col("creation_timestamp") \
    #            .between(*timestamp_range(base_time, 1, 'm'))) \
    #     .groupBy("session_id").agg( \
    #     max("creation_timestamp").alias("service_leave"), \
    #     min("creation_timestamp").alias("service_enter") \
    #     ).withColumn("duration", col("service_leave") - col("service_enter")) \
    #     .show()

    hive_df = cassandra_df.select("*") \
        .where(col("creation_timestamp") \
               .between(*timestamp_range(base_time, 10000, 'D')))

    hive_df.write.mode("append") \
        .format("hive") \
        .partitionBy("service_id") \
        .saveAsTable("test.weblogs")

# 간편한 between 연산을 위해 만든 유틸리티 함수
# base_time: 기준 시간
# interval: 기분 시간으로부터 얼마나 조회를 할 지의 범위
# 초, 분, 시 등의 단위
# ex. timestamp_range("2023-03-21 13:49:00", 10, 'm') => 2023-03-21 13:49:00 부터 10분 이후의 시간까지
def timestamp_range(base_time, interval, unit):
    dt_obj = datetime.strptime(base_time, '%Y-%m-%d %H:%M:%S')
    if unit == 's':
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(seconds=interval))
        else:
            return (dt_obj - timedelta(seconds=-interval), dt_obj)
    if unit == 'm':
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(minutes=interval))
        else:
            return (dt_obj - timedelta(minutes=-interval), dt_obj)
    if unit == 'H':
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(hours=interval))
        else:
            return (dt_obj - timedelta(hours=-interval), dt_obj)
    if unit == 'D':
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(days=interval))
        else:
            return (dt_obj - timedelta(days=-interval), dt_obj)
    if unit == 'M':
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(months=interval))
        else:
            return (dt_obj - timedelta(months=-interval), dt_obj)
    if unit == 'Y':
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(years=interval))
        else:
            return (dt_obj - timedelta(years=-interval), dt_obj)

    # # Create Hive schema
    # schema = StructType([
    #     StructField("id", IntegerType(), True),
    #     StructField("name", StringType(), True)
    # ])
    #
    # # Create Hive table
    # hive_table = 'hive_table'
    # cassandra_df.write.format("hive").option("table", hive_table).option("schema", "default").save()