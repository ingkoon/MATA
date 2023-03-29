import findspark
findspark.init("/usr/local/lib/spark-3.3.2-bin-hadoop3")

from pyspark import SparkConf
from pyspark.sql.functions import *
from pyspark.sql.types import *
from pyspark.sql import *

import pandas as pd
from cassandra.cluster import Cluster
from cassandra.query import dict_factory

from datetime import datetime
from datetime import timedelta
from dateutil.relativedelta import relativedelta
from time import mktime

# 간편한 between 연산을 위해 만든 유틸리티 함수
# base_time: 기준 시간
# interval: 기분 시간으로부터 얼마나 조회를 할 지의 범위
# 초, 분, 시 등의 단위
# ex. timestamp_range("2023-03-21 13:49:00", 10, 'm') => 2023-03-21 13:49:00 부터 10분 이후의 시간까지
def timestamp_range(base_time, interval, unit):
    dt_obj = datetime.strptime(base_time, '%Y-%m-%d %H:%M:%S')
    if unit=='s':
        if interval>=0:
            return (dt_obj, dt_obj+timedelta(seconds=interval))
        else:
            return (dt_obj-timedelta(seconds=-interval), dt_obj)
    if unit=='m':
        if interval>=0:
            return (dt_obj, dt_obj+timedelta(minutes=interval))
        else:
            return (dt_obj-timedelta(minutes=-interval), dt_obj)
    if unit=='h':
        if interval>=0:
            return (dt_obj, dt_obj+timedelta(hours=interval))
        else:
            return (dt_obj-timedelta(hours=-interval), dt_obj)
    if unit=='d':
        if interval>=0:
            return (dt_obj, dt_obj+timedelta(days=interval))
        else:
            return (dt_obj-timedelta(days=-interval), dt_obj)
    if unit=='w':
        if interval>=0:
            return (dt_obj, dt_obj+timedelta(days=interval*7))
        else:
            return (dt_obj-timedelta(days=-interval*7), dt_obj)
    if unit=='mo':
        if interval>=0:
            return (dt_obj, dt_obj+relativedelta(months=interval))
        else:
            return (dt_obj-relativedelta(months=-interval), dt_obj)
    if unit=='y':
        if interval>=0:
            return (dt_obj, dt_obj+relativedelta(years=interval))
        else:
            return (dt_obj-relativedelta(years=-interval), dt_obj)

def pandas_factory(colnames, rows):
    return pd.DataFrame(rows, columns=colnames)


##### 1분 짜리는 spark 대신 pandas를 사용해서 1개의 worker로 처리
def batching_cassandra_pandas(base_time, amount, unit):
    if str(amount)+unit not in ["1m"]:
        print("invalid interval: interval should be 1m, 5m, 10m, 30m, 1h, 6h or 12h.")
        return 2

    base_timestamp = datetime.timestamp(datetime.strptime(base_time, '%Y-%m-%d %H:%M:%S'))

    cassandra_keyspace = "tagmanager"
    cassandra_table = "stream"

    cluster = Cluster(['master01'])  # Cassandra 클러스터 노드의 IP 주소 입력
    session = cluster.connect('tagmanager')  # keyspace 이름 입력

    session.row_factory = pandas_factory
    session.default_fetch_size = None

    start, end = timestamp_range(base_time, -amount, unit)

    sql_query = "SELECT * FROM {}.{} WHERE creation_timestamp >= '{}' AND creation_timestamp <= '{}' ALLOW FILTERING".format(
        CASSANDRA_DB, CASSANDRA_TABLE, start, end)
    rslt = session.execute(sql_query, timeout=None)
    df = rslt._current_rows

    # component_df = batch_df[batch_df['creation_timestamp'].between(*timestamp_range(base_time, -amount, unit))]

    component_df = df[df['event'].str.contains('click')]
    component_df = component_df.groupby(['service_id', 'target_id', 'location']).agg(total_click=('key', 'count'))
    component_df['update_timestamp'] = pd.to_datetime(base_timestamp)
    component_df = component_df.reset_index()[
        ['total_click', 'target_id', 'location', 'update_timestamp', 'service_id']]


##### Cassandra -> Hive Batching (spark 사용)
##### 1분부터 12시간까지 Cassandra 데이터를 기준으로 집계
def batching_cassandra_spark(base_time, amount, unit):
    if str(amount)+unit not in ["5m", "10m", "30m", "1h", "6h", "12h"]:
        print("invalid interval: interval should be 1m, 5m, 10m, 30m, 1h, 6h or 12h.")
        return 2
    
    base_timestamp = datetime.timestamp(datetime.strptime(base_time, '%Y-%m-%d %H:%M:%S'))

    cassandra_keyspace = "tagmanager"
    cassandra_table = "stream"

    session = SparkSession.builder \
        .appName("Batching_Cassandra_To_Hive") \
        .master("yarn") \
        .config("spark.yarn.queue", "batch") \
        .config("spark.jars.packages",
                "org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.2,com.datastax.spark:spark-cassandra-connector_2.12:3.3.0") \
        .config("spark.hadoop.hive.exec.dynamic.partition.mode", "nonstrict") \
        .enableHiveSupport() \
        .getOrCreate()

    batch_df = session.read \
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
          .load()

    #########
    # components 테이블 집계
    component_df = batch_df.select("*") \
        .where(col("creation_timestamp") \
                .between(*timestamp_range(base_time, -amount, unit))) \
        .where(col("event").like("click")) \
        .groupBy("service_id", "target_id", "location").agg( \
            count("key").alias("total_click"), \
        ).withColumn("update_timestamp", lit(base_timestamp).cast("timestamp")) \
        .select("total_click", "target_id", "location", "update_timestamp", "service_id")
    component_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.components_{}{}".format(str(amount), unit))

    #########
    # clicks 테이블 집계
    click_df = batch_df.select("*") \
        .where(col("creation_timestamp") \
                .between(*timestamp_range(base_time, -amount, unit))) \
        .where(col("event").like("click")) \
        .groupBy("service_id", "position_x", "position_y", "location").agg( \
            count("key").alias("total_click"), \
        ).withColumn("update_timestamp", lit(base_timestamp).cast("timestamp")) \
        .select("total_click", "position_x", "position_y","location", "update_timestamp", "service_id")
    click_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.clicks_{}{}".format(str(amount), unit))

    #########
    # page_durations 테이블 집계
    page_durations_df = batch_df.select("*") \
        .where(col("creation_timestamp") \
                .between(timestamp_range(base_time, -amount, unit)[0]-timedelta(minutes=30), timestamp_range(base_time, -amount, unit)[1])) \
        .groupBy("service_id", "location", "session_id").agg( \
            avg("page_duration").alias("page_duration"), \
        ).groupBy("service_id", "location").agg( \
            count("session_id").alias("total_session"), \
            sum("page_duration").alias("total_duration") \
        ).withColumn("update_timestamp", lit(base_timestamp).cast("timestamp")) \
        .select("total_duration","total_session","location", "update_timestamp","service_id")
    page_durations_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_durations_{}{}".format(str(amount), unit))

    #########
    # page_journals 테이블 집계
    page_journals_df = batch_df.select("*") \
        .where(col("creation_timestamp") \
                .between(*timestamp_range(base_time, -amount, unit))) \
        .where(col("event").like("pageenter")) \
        .groupBy("service_id", "prev_location", "location").agg( \
            count("key").alias("total_journal"),\
         ).withColumn("update_timestamp", lit(base_timestamp).cast("timestamp")) \
    .select("total_journal", col("prev_location").alias("location_from"), col("location").alias("location_to"), "update_timestamp","service_id")
    page_journals_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_journals_{}{}".format(str(amount), unit))

    #########
    # page_refers 테이블 집계
    refer_df = batch_df \
        .where(col("creation_timestamp") \
               .between(*timestamp_range(base_time, -amount, unit))) \
        .withColumn("referrer", split(batch_df.referrer, "/").getItem(2)) \
        .groupBy("referrer", "service_id") \
        .agg(countDistinct("session_id").alias("total_session"),
             sum(when(col("event") == "pageenter", 1).otherwise(0)).alias("total_pageenter")
             ) \
        .withColumn("update_timestamp", current_timestamp()) \
        .select("total_session", "total_pageenter", "update_timestamp", "referrer", "service_id")

    refer_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_refers_{}{}".format(str(amount), unit))

    
    session.stop()


##### Hive -> Hive Batching
##### 1일부터 1년까지 12시간 집계 데이터를 기준으로 집계
def batching_hive(base_time, amount, unit):
    if str(amount)+unit not in ["1d", "1w", "1mo", "6mo", "1y"]:
        print("invalid interval: interval should be 1d, 1w, 1mo, 6mo or     1y.")
        return 2

    session = SparkSession.builder \
        .appName("Batching_Hive_To_Hive") \
        .master("yarn") \
        .config("spark.yarn.queue", "batch") \
        .config("spark.hadoop.hive.exec.dynamic.partition.mode", "nonstrict") \
        .enableHiveSupport() \
        .getOrCreate()
    
    base_timestamp = datetime.timestamp(datetime.strptime(base_time, '%Y-%m-%d %H:%M:%S'))

    component_df = session.read \
        .format("hive") \
        .table("mata.components_12h") \
        .select("*") \
        .where(col("update_timestamp") \
                .between(*timestamp_range(base_time, -amount, unit))) \
        .groupBy("service_id", "target_id", "location").agg(
            sum("total_click").alias("total_click") \
        ).withColumn("update_timestamp", lit(base_timestamp).cast("timestamp")) \
        .select("total_click", "target_id", "location", "update_timestamp", "service_id")
    component_df.show()
    component_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.components_{}{}".format(amount, unit))

    #########
    # clicks 테이블 집계
    click_df = session.read \
        .format("hive") \
        .table("mata.clicks_12h") \
        .select("*") \
        .where(col("update_timestamp") \
                .between(*timestamp_range(base_time, -amount, unit))) \
        .groupBy("service_id", "position_x", "position_y", "location").agg( \
            sum("total_click").alias("total_click"), \
        ).withColumn("update_timestamp", lit(base_timestamp).cast("timestamp")) \
        .select("total_click", "position_x", "position_y","location", "update_timestamp", "service_id")
    click_df.show()
    click_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.clicks_{}{}".format(amount, unit))

    #########
    # page_durations 테이블 집계
    page_durations_df = session.read \
        .format("hive") \
        .table("mata.page_durations_12h") \
        .select("*") \
        .where(col("update_timestamp") \
                .between(*timestamp_range(base_time, -amount, unit))) \
        .groupBy("service_id", "location").agg(\
            sum("total_session").alias("total_session"),\
            sum("total_duration").alias("total_duration"),\
         ).withColumn("update_timestamp", lit(base_timestamp).cast("timestamp")) \
        .select("total_duration","total_session","location", "update_timestamp","service_id")
    page_durations_df.show()
    page_durations_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_durations_{}{}".format(amount, unit))

    #########
    # page_journals 테이블 집계
    page_journals_df = session.read \
        .format("hive") \
        .table("mata.page_journals_12h") \
        .select("*") \
        .where(col("update_timestamp") \
                .between(*timestamp_range(base_time, -amount, unit))) \
        .groupBy("service_id", "location_from", "location_to",).agg(\
            sum("total_journal").alias("total_journal"),\
         ).withColumn("update_timestamp", lit(base_timestamp).cast("timestamp")) \
    .select("total_journal", "location_from", "location_to", "update_timestamp", "service_id")
    page_journals_df.show()
    page_journals_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_journals_{}{}".format(amount, unit))
    
    session.stop()