
from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from datetime import datetime, timedelta
from lib.spark.spark_job import read_cassandra_to_spark

default_args = {
    'owner': 'airflow',
    'start_date': datetime(2023, 3, 23),
    'schedule_interval' : timedelta(minutes=1),
    'retry_delay': timedelta(minutes=1),
}

with DAG('CassandraToHive', default_args=default_args, schedule_interval=None) as dag:

    read_cassandra_to_spark = PythonOperator(
        task_id='read_cassandra_to_spark',
        python_callable=read_cassandra_to_spark
    )

    read_cassandra_to_spark