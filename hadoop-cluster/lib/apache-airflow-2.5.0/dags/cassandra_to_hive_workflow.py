
from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from datetime import datetime, timedelta
from lib.spark.cassandra_to_hive_job import read_cassandra_to_spark

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
    python_callable=read_cassandra_to_spark,
    dag=dag
)

cassandra_to_spark