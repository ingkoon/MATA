from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from datetime import datetime, timedelta
# from lib.spark.kafka_to_cassandra_job import kafka_to_cassandra_pipeline
from lib.spark.Streaming_Jobs import streaming_job

default_args = {
    'owner': 'airflow',
    'start_date': datetime(2023, 3, 27, 0, 00),
    'end_date' : datetime(2023, 3, 27, 1, 30),
    'retry_delay': timedelta(minutes=1),
}

dag = DAG(
    'kafka_to_cassandra',
    default_args=default_args,
    schedule_interval=None,
    description='Kafka to Cassandra'
)

kafka_to_cassandra = PythonOperator(
    task_id='kafka_to_cassandra',
    python_callable=streaming_job,
    dag=dag
)

kafka_to_cassandra