
from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from datetime import datetime, timedelta
from lib.spark.cassandra_to_hive_job import batching_cassandra

default_args = {
    'owner': 'airflow',
    'start_date': datetime(2023, 3, 24, 4, 35, 00),
    'retry_delay': timedelta(minutes=1),
}

dag = DAG(
    'cassandra_to_hive',
    default_args=default_args,
    description='Cassandra to Hive',
    schedule_interval=timedelta(minutes=1)
)

cassandra_to_spark = PythonOperator(
    task_id='cassandra_to_hive',
    python_callable=batching_cassandra,
    params = {"base_time" : "2023-03-27 8:45:00",
              "amount" : "10",
              "unit" : "D"},
    dag=dag
)

cassandra_to_spark