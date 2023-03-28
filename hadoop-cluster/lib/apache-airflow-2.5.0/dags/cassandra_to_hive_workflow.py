
from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from datetime import datetime, timedelta
# from lib.spark.cassandra_to_hive_job import batching_cassandra
from lib.spark.Batching_Jobs import batching_hive, batching_cassandra

default_args = {
    'owner': 'airflow',
    'start_date': datetime(2023, 3, 28, 1, 37, 00),
    'retry_delay': timedelta(minutes=1),
}

dag1m = DAG(
    'cassandra_to_hive_1m',
    default_args=default_args,
    description='Cassandra to Hive',
    schedule_interval=timedelta(minutes=1)
)

dag5m = DAG(
    'cassandra_to_hive_5m',
    default_args=default_args,
    description='Cassandra to Hive',
    schedule_interval=timedelta(minutes=5)
)

dag10m = DAG(
    'cassandra_to_hive_10m',
    default_args=default_args,
    description='Cassandra to Hive',
    schedule_interval=timedelta(minutes=10)
)

dag30m = DAG(
    'cassandra_to_hive_30m',
    default_args=default_args,
    description='Cassandra to Hive',
    schedule_interval=timedelta(minutes=30)
)

dag1h = DAG(
    'cassandra_to_hive_1h',
    default_args=default_args,
    description='Cassandra to Hive',
    schedule_interval=timedelta(hours=1)
)

dag6h = DAG(
    'cassandra_to_hive_6h',
    default_args=default_args,
    description='Cassandra to Hive',
    schedule_interval=timedelta(hours=6)
)

dag12h = DAG(
    'cassandra_to_hive_12h',
    default_args=default_args,
    description='Cassandra to Hive',
    schedule_interval=timedelta(hours=12)
)

cassandra_to_spark_1m = PythonOperator(
    task_id='cassandra_to_spark_1m',
    python_callable=batching_cassandra,
    op_kwargs = {"base_time" : "2023-03-28 00:00:00",
                 "amount" : 1,
                 "unit" : "m"},
    dag=dag1m
)

cassandra_to_spark_5m = PythonOperator(
    task_id='cassandra_to_spark_5m',
    python_callable=batching_cassandra,
    op_kwargs = {"base_time" : "2023-03-28 00:00:00",
                 "amount" : 5,
                 "unit" : "m"},
    dag=dag5m
)

cassandra_to_spark_10m = PythonOperator(
    task_id='cassandra_to_spark_10m',
    python_callable=batching_cassandra,
    op_kwargs = {"base_time" : "2023-03-28 00:00:00",
                 "amount" : 10,
                 "unit" : "m"},
    dag=dag10m
)

cassandra_to_spark_30m = PythonOperator(
    task_id='cassandra_to_spark_30m',
    python_callable=batching_cassandra,
    op_kwargs = {"base_time" : "2023-03-28 00:00:00",
                 "amount" : 30,
                 "unit" : "m"},
    dag=dag30m
)

cassandra_to_spark_1h = PythonOperator(
    task_id='cassandra_to_spark_1h',
    python_callable=batching_cassandra,
    op_kwargs = {"base_time" : "2023-03-28 00:00:00",
                 "amount" : 1,
                 "unit" : "h"},
    dag=dag1h
)

cassandra_to_spark_6h = PythonOperator(
    task_id='cassandra_to_spark_6h',
    python_callable=batching_cassandra,
    op_kwargs = {"base_time" : "2023-03-28 00:00:00",
                 "amount" : 6,
                 "unit" : "h"},
    dag=dag6h
)

cassandra_to_spark_12h = PythonOperator(
    task_id='cassandra_to_spark_12h',
    python_callable=batching_cassandra,
    op_kwargs = {"base_time" : "2023-03-28 00:00:00",
                 "amount" : 12,
                 "unit" : "h"},
    dag=dag12h
)



cassandra_to_spark_1m, cassandra_to_spark_5m, cassandra_to_spark_10m, cassandra_to_spark_30m, cassandra_to_spark_1h, cassandra_to_spark_6h, cassandra_to_spark_12h