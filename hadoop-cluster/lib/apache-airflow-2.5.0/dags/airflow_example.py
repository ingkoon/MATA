from airflow import DAG
from airflow.operators.bash_operator import BashOperator
from airflow.operators.hive_operator import HiveOperator
from datetime import datetime, timedelta

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'start_date': datetime(2023, 3, 22),
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

dag = DAG(
    'web_logs_to_hive',
    default_args=default_args,
    description='Save web logs data from Cassandra to Hive',
    schedule_interval=timedelta(days=1),
)

# Create Hive table to store web logs data
create_table_query = """
CREATE TABLE IF NOT EXISTS project (
    projectId STRING,
    url STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE
LOCATION 'hdfs:///user/hive/warehouse';
"""

create_table = HiveOperator(
    task_id='create_hive_table',
    hql=create_table_query,
    hive_cli_conn_id='cassandra_to_hive',
    dag=dag,
)

# Save web logs data from Cassandra to Hive
save_data_query = """
INSERT OVERWRITE TABLE web_logs
SELECT date, time, ip, method, url, response_code, user_agent
FROM cassandra_web_logs
WHERE date = '{{ ds }}'
"""

save_data = HiveOperator(
    task_id='save_web_logs_data',
    hql=save_data_query,
    hive_cli_conn_id='cassandra_to_hive',
    dag=dag,
)

create_table >> save_data
