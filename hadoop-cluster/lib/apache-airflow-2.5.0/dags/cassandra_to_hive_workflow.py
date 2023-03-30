
from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from datetime import datetime, timedelta
from dateutil.relativedelta import relativedelta
# from lib.spark.cassandra_to_hive_job import batching_cassandra
from lib.spark.Batching_Jobs import batching_hive, batching_cassandra_spark, batching_hive_all

default_args = {
    'owner': 'airflow',
    'start_date': datetime(2023, 3, 30, 4, 30),
    'retries': 1,
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

dag1d = DAG(
    'hive_to_hive_1d',
    default_args=default_args,
    description='Hive to Hive',
    schedule_interval= timedelta(days=1)
)

dag1w = DAG(
    'hive_to_hive_1w',
    default_args=default_args,
    description='Hive to Hive',
    schedule_interval=timedelta(weeks=1)
)

dag1mo = DAG(
    'hive_to_hive_1mo',
    default_args=default_args,
    description='Hive to Hive',
    schedule_interval=relativedelta(months=1)
)

dag6mo = DAG(
    'hive_to_hive_6mo',
    default_args=default_args,
    description='Hive to Hive',
    schedule_interval=relativedelta(months=6)
)

dag1y = DAG(
    'hive_to_hive_1y',
    default_args=default_args,
    description='Hive to Hive',
    schedule_interval=relativedelta(years=1)
)

dagall = DAG(
    'hive_to_hive_all',
    default_args=default_args,
    description='Hive to Hive',
    schedule_interval=timedelta(days=1)
)



now = datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S')

cassandra_to_spark_1m = PythonOperator(
    task_id='cassandra_to_spark_1m',
    python_callable=batching_cassandra_spark,
    op_kwargs = {"base_time" : now,
                 "amount" : 1,
                 "unit" : "m"},
    dag=dag1m
)

cassandra_to_spark_5m = PythonOperator(
    task_id='cassandra_to_spark_5m',
    python_callable=batching_cassandra_spark,
    op_kwargs = {"base_time" : now,
                 "amount" : 5,
                 "unit" : "m"},
    dag=dag5m
)

cassandra_to_spark_10m = PythonOperator(
    task_id='cassandra_to_spark_10m',
    python_callable=batching_cassandra_spark,
    op_kwargs = {"base_time" : now,
                 "amount" : 10,
                 "unit" : "m"},
    dag=dag10m
)

cassandra_to_spark_30m = PythonOperator(
    task_id='cassandra_to_spark_30m',
    python_callable=batching_cassandra_spark,
    op_kwargs = {"base_time" : now,
                 "amount" : 30,
                 "unit" : "m"},
    dag=dag30m
)

cassandra_to_spark_1h = PythonOperator(
    task_id='cassandra_to_spark_1h',
    python_callable=batching_cassandra_spark,
    op_kwargs = {"base_time" : now,
                 "amount" : 1,
                 "unit" : "h"},
    dag=dag1h
)

cassandra_to_spark_6h = PythonOperator(
    task_id='cassandra_to_spark_6h',
    python_callable=batching_cassandra_spark,
    op_kwargs = {"base_time" : now,
                 "amount" : 6,
                 "unit" : "h"},
    dag=dag6h
)

cassandra_to_spark_12h = PythonOperator(
    task_id='cassandra_to_spark_12h',
    python_callable=batching_cassandra_spark,
    op_kwargs = {"base_time" : now,
                 "amount" : 12,
                 "unit" : "h"},
    dag=dag12h
)

hive_to_spark_1d = PythonOperator(
    task_id='hive_to_spark_1d',
    python_callable=batching_hive,
    op_kwargs = {"base_time" : now,
                 "amount" : 1,
                 "unit" : "d"},
    dag=dag1d
)

hive_to_spark_1w = PythonOperator(
    task_id='hive_to_spark_1w',
    python_callable=batching_hive,
    op_kwargs = {"base_time" : now,
                 "amount" : 1,
                 "unit" : "w"},
    dag=dag1w
)

hive_to_spark_1mo = PythonOperator(
    task_id='hive_to_spark_1mo',
    python_callable=batching_hive,
    op_kwargs = {"base_time" : now,
                 "amount" : 1,
                 "unit" : "mo"},
    dag=dag1mo
)

hive_to_spark_6mo = PythonOperator(
    task_id='hive_to_spark_6mo',
    python_callable=batching_hive,
    op_kwargs = {"base_time" : now,
                 "amount" : 6,
                 "unit" : "mo"},
    dag=dag6mo
)

hive_to_spark_1y = PythonOperator(
    task_id='hive_to_spark_1y',
    python_callable=batching_hive,
    op_kwargs = {"base_time" : now,
                 "amount" : 1,
                 "unit" : "y"},
    dag=dag1y
)

hive_to_spark_all = PythonOperator(
    task_id='hive_to_spark_all',
    python_callable=batching_hive_all,
    op_kwargs = {"base_time" : now,
                 "amount" : 10000,
                 "unit" : "d"},
    dag=dagall
)





# cassandra_to_spark_1m, cassandra_to_spark_5m, cassandra_to_spark_10m, cassandra_to_spark_30m, cassandra_to_spark_1h, cassandra_to_spark_6h, cassandra_to_spark_12h