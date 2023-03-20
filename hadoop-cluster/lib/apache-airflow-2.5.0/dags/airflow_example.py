from airflow import DAG
from airflow.operators.python_operator import PythonOperator
from datetime import datetime, timedelta

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'start_date': datetime(2023, 3, 20),
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=5)
}

dag = DAG(
    'cassandra_to_hive',
    default_args=default_args,
    description='Cassandra to Hive data pipeline',
    schedule_interval=timedelta(hours=1)
)

def cassandra_to_hive():
    # Cassandra 연결
    cluster = Cluster(['cassandra_host'])
    session = cluster.connect('keyspace_name')

    # Cassandra에서 데이터 가져오기
    rows = session.execute('SELECT * FROM users')

    # Pandas DataFrame으로 변환
    df = pd.DataFrame(list(rows))
    df.columns = rows.column_names

    # Hive 연결
    conn = hive.connect(host='hive_host', port=10000, username='hive_user')
    cursor = conn.cursor()

    # Hive 테이블 생성
    cursor.execute('CREATE TABLE IF NOT EXISTS web_user_logs (id INT, username STRING, activity STRING, timestamp TIMESTAMP)')

    # 데이터 저장
    for index, row in df.iterrows():
        query = f"INSERT INTO web_user_logs VALUES ({row['id']}, '{row['username']}', '{row['activity']}', '{row['timestamp']}')"
        cursor.execute(query)

    # Hive 연결 종료
    cursor.close()
    conn.close()

run_this = PythonOperator(
    task_id='cassandra_to_hive',
    python_callable=cassandra_to_hive,
    dag=dag
)

run_this