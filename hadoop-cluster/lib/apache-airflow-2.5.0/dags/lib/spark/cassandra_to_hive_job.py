
import findspark
findspark.init("/usr/local/lib/spark-3.3.2-bin-hadoop3")

from pyspark.sql.functions import *
from pyspark.sql.types import *
from pyspark.sql import *
from pyspark.sql.functions import udf, col, from_json, pandas_udf, split, count

from datetime import datetime
from datetime import timedelta


# 간편한 between 연산을 위해 만든 유틸리티 함수
# base_time: 기준 시간
# interval: 기분 시간으로부터 얼마나 조회를 할 지의 범위
# 초, 분, 시 등의 단위
# ex. timestamp_range("2023-03-21 13:49:00", 10, 'm') => 2023-03-21 13:49:00 부터 10분 이후의 시간까지
def timestamp_range(base_time, interval, unit):
    dt_obj = datetime.strptime(base_time, '%Y-%m-%d %H:%M:%S')
    if unit == "s":
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(seconds=interval))
        else:
            return (dt_obj - timedelta(seconds=-interval), dt_obj)
    if unit == "m":
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(minutes=interval))
        else:
            return (dt_obj - timedelta(minutes=-interval), dt_obj)
    if unit == "h":
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(hours=interval))
        else:
            return (dt_obj - timedelta(hours=-interval), dt_obj)
    if unit == "d":
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(days=interval))
        else:
            return (dt_obj - timedelta(days=-interval), dt_obj)
    if unit == "mo":
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(months=interval))
        else:
            return (dt_obj - timedelta(months=-interval), dt_obj)
    if unit == "y":
        if interval >= 0:
            return (dt_obj, dt_obj + timedelta(years=interval))
        else:
            return (dt_obj - timedelta(years=-interval), dt_obj)


##### Cassandra -> Hive Batching
##### 1분부터 12시간까지 Cassandra 데이터를 기준으로 집계
def batching_cassandra(base_time, amount, unit):
    if str(amount) + unit not in ["1m", "5m", "10m", "30m", "1h", "6h", "12h"]:
        print("invalid interval: interval should be 1m, 5m, 10m, 30m, 1h, 6h or 12h.")
        return 2

    session = SparkSession.builder \
        .appName("Batching_Cassandra_To_Hive") \
        .master("yarn") \
        .config("spark.yarn.queue", "batch") \
        .config("spark.jars.packages",
                "org.apache.spark:spark-sql-kafka-0-10_2.12:3.3.2,com.datastax.spark:spark-cassandra-connector_2.12:3.3.0") \
        .config("spark.hadoop.hive.exec.dynamic.partition.mode", "nonstrict") \
        .enableHiveSupport() \
        .getOrCreate()

    base_timestamp = datetime.timestamp(datetime.strptime(base_time, '%Y-%m-%d %H:%M:%S'))

    cassandra_keyspace = "tagmanager"
    cassandra_table = "stream"
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
    component_df = batch_df.select("total_click", "target_id", "location", "update_timestamp", "service_id") \
        .where(col("creation_timestamp") \
               .between(*timestamp_range(base_time, amount, unit))) \
        .where(col("event").like("click")) \
        .groupBy("service_id", "target_id", "location").agg( \
        count("key").alias("total_click"), \
        ).withColumn("update_timestamp", base_timestamp) \
        .select("total_click", "target_id", "location", "update_timestamp", "service_id")
    component_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.components_{}{}".format(str(amount), unit))

    #########
    # clicks 테이블 집계
    click_df = batch_df.select("*") \
        .where(col("creation_timestamp") \
               .between(*timestamp_range(base_time, amount, unit))) \
        .where(col("event").like("click")) \
        .groupBy("service_id", "position_x", "position_y", "location").agg( \
        count("key").alias("total_click"), \
        ).withColumn("update_timestamp", base_timestamp) \
        .select("total_click", "position_x", "position_y", "location", "update_timestamp", "service_id")
    click_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.clicks_{}{}".format(str(amount), unit))

    #########
    # page_durations 테이블 집계
    page_durations_df = batch_df.select("*") \
        .where(col("creation_timestamp") \
               .between(timestamp_range(base_time, amount, unit))) \
        .groupBy("service_id", "location", "service_id").agg( \
        count("").alias("total_session"), \
        sum("page_duration").alias("total_duration"), \
        ).withColumn("update_timestamp", base_timestamp) \
        .select("total_duration", "total_session", "location", "update_timestamp", "service_id")
    page_durations_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_durations_{}{}".format(str(amount), unit))

    #########
    # page_journals 테이블 집계
    page_journals_df = batch_df.select("*") \
        .where(col("creation_timestamp") \
               .between(timestamp_range(base_time, amount, unit))) \
        .groupBy("service_id", "prev_location", "location").agg( \
        count("").alias("total_journal"), \
        ).withColumn("update_timestamp", base_timestamp) \
        .select("total_journal", col("prev_location").alias("location_from"), col("location").alias("location_to"),
                "update_timestamp", "service_id")
    page_journals_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_journals_{}{}".format(str(amount), unit))

    session.stop()


##### Hive -> Hive Batching
##### 1일부터 1년까지 12시간 집계 데이터를 기준으로 집계
def batching_hive(base_date, amount, unit):
    if str(amount) + unit not in ["1d", "1w", "1mo", "6mo", "1y"]:
        print("invalid interval: interval should be 1d, 1w, 1mo, 6mo or 1y.")
        return 2

    session = SparkSession.builder \
        .appName("Batching_Hive_To_Hive") \
        .master("yarn") \
        .config("spark.yarn.queue", "batch") \
        .config("spark.hadoop.hive.exec.dynamic.partition.mode", "nonstrict") \
        .enableHiveSupport() \
        .getOrCreate()

    base_timestamp = datetime.timestamp(datetime.strptime(base_time, '%Y-%m-%d %H:%M:%S'))

    #########
    # components 테이블 집계
    component_df = session.read \
        .format("hive") \
        .table("mata.components_12h") \
        .select("*") \
        .where(col("update_timestamp") \
               .between(*timestamp_range(base_time, -amount, unit))) \
        .groupBy("service_id", "target_id", "location").agg(
        sum("total_click").alias("total_click") \
        ).withColumn("update_timestamp", base_timestamp) \
        .select("total_click", "target_id", "location", "update_timestamp", "service_id")
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
        ).withColumn("update_timestamp", base_timestamp) \
        .select("total_click", "position_x", "position_y", "location", "update_timestamp", "service_id")
    click_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.clicks_{}{}".format(amount, unit))

    #########
    # page_durations 테이블 집계
    page_durations_df = session.read \
        .format("hive") \
        .table("mata.page_durations_12h") \
        .select("*") \
        .where(col("creation_timestamp") \
               .between(timestamp_range(base_time, -amount, unit))) \
        .groupBy("service_id", "location").agg( \
        sum("total_session").alias("total_session"), \
        sum("total_duration").alias("total_duration"), \
        ).withColumn("update_timestamp", base_timestamp) \
        .select("total_duration", "total_session", "location", "update_timestamp", "service_id")
    page_durations_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_durations_{}{}".format(amount, unit))

    #########
    # page_journals 테이블 집계
    page_journals_df = session.read \
        .format("hive") \
        .table("mata.page_journals_12h") \
        .select("*") \
        .where(col("creation_timestamp") \
               .between(timestamp_range(base_time, -amount, unit))) \
        .groupBy("service_id", "location_from", "location_to", ).agg( \
        sum("total_journal").alias("total_journal"), \
        ).withColumn("update_timestamp", base_timestamp) \
        .select("total_journal", "location_from" "location_to", "update_timestamp", "service_id")
    page_journals_df.write.mode("append") \
        .format("hive") \
        .insertInto("mata.page_journals_{}{}".format(amount, unit))

    session.stop()