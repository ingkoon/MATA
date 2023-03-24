from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from datetime import datetime, timedelta
from lib.spark.kafka_to_cassandra_job import kafka_to_cassandra_pipeline

default_args = {
    'owner': 'airflow',
    'start_date': datetime(2023, 3, 23),
    'retry_delay': timedelta(minutes=1),
}

dag = DAG(
    'kafka_to_cassandra',
    default_args=default_args,
    description='Kafka to Cassandra'
)

kafka_to_cassandra = PythonOperator(
    task_id='kafka_to_cassandra',
    python_callable=kafka_to_cassandra_pipeline,
    dag=dag
)

kafka_to_cassandra