from airflow import DAG
from airflow.operators.bash_operator import BashOperator
from datetime import datetime, timedelta

default_args = {
    'owner': 'my_username',
    'depends_on_past': False,
    'start_date': datetime(2023, 3, 22),
    'email': ['my_email@example.com'],
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=1),
}

dag = DAG('cassandra_to_hive', default_args=default_args, schedule_interval=timedelta(minutes=1))

with dag:
    t1 = BashOperator(
        task_id='run_spark_job',
        bash_command='spark-submit /path/to/spark_job.py',
        dag=dag
    )

t1