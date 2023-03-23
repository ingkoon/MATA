# Tag Manager 진행상황 문서

## 0. 데이터 플로우

현재까지 진행된 셋업은 다음과 같다.
- React -> SpringBoot -> Kafka -> Spark -> Cassandra
클러스터를 실행하기 위해선 다음과 같이 진행한다.
```
# 1. hosts 등록
/> sudo vim /etc/hosts
## 상단에 다음의 내용 추가
172.16.238.2    master01
172.16.238.3    master02
172.16.238.4    slave01
172.16.238.5    slave02
172.16.238.6    slave03
172.16.238.7    metastore
172.16.238.8    client

# 2. 클러스터 실행
S08P22A506/hadoop-cluster> sudo bash run.sh
```

## 1. React
최신버전의 NodeJS를 설치해야 한다.
```
apt install -y curl build-essential
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
export NVM_DIR="$([ -z "${XDG_CONFIG_HOME-}" ] && printf %s "${HOME}/.nvm" || printf %s "${XDG_CONFIG_HOME}/nvm")"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
nvm install node
```
다음과 같이 테스트용 React 서버를 실행한다.
```
S08P22A506/vue3> npm i
S08P22A506/vue3> npm start
```

## 2. SpringBoot
Spring 서버가 정상적으로 실행되기 위해선 MySQL과 Kafka에 성공적으로 연결 되어야 한다.  
MySQL은 `metastore` 컨테이너, Kafka는 `master01` ~ `slave03` 컨테이너에서 실행되고 있다.
SpringBoot의 설정파일인 application.yml은 `dev` 프로필로 실행되어야 한다.
```
# 빌드가 되어있을 경우
java -jar -Dspring.profiles.active=dev target/[JAR 아카이브]
# IDE 환경일 경우
[dev 프로필로 ApiApplication 실행]
```

## 3. Kafka
SpringBoot는 Kafka의 Producer이다.  
Topic은 `tagmanager`이며, `run.sh` 실행을 통해 지동으로 추가되는 topic이다.
정상적으로 연결이 되었는지 확인하기 위해선, 다음과 같이 `console-consumer`를 실행하여 확인할 수 있다.
```
> sudo docker attach master01
[master01]> kafka-console-consumer.sh --topic tagmanager --from-beginning --bootstrap-server localhost:9092
```
주의할 사항은, 이미 produce된 메시지는 무조건 consume할 수 밖에 없기에,  
스키마 변경 이후에는 클러스터를 재시작 해야한다.

## 4. Spark
테스트 환경을 위해서 `client` 컨테이너의 Jupyter Notebook 환경을 이용하거나,  
로컬에 spark 의존성을 추가한 뒤 스크립트로 테스트 해도 무방하다.
`client` 컨테이너에서 Jupyter Notebook을 실행하기 위한 명령은 다음과 같다.
실행된 Jupyter Lab Web UI는 localhost:19900 또는 client:9090으로 접근할 수 있다.
```
> sudo docker exec -it client bash
[client]> hadoop fs -mkdir -p /user/spark/event/log
[client]> jupyter lab --ip=0.0.0.0 --port=9090 --allow-root
```
`SparkWithCassandra.ipynb` 파일에서 예시 코드를 확일할 수 있다.
hadoop-cluster/node/lib/spark-3.3.2-bin-hadoop3/jobs 경로의 `kafka_to_cassandra` 스크립트는  
Kafka source 로 부터 Cassandra sink 까지 데이터를 전처리 하는 spark job 이다.  
전처리 되기 전의 스키마는 다음과 같다.
```
root
 |-- key: string (nullable = true)
 |-- value: struct (nullable = true)
 |    |-- serviceToken: string (nullable = true)
 |    |-- clientId: long (nullable = true)
 |    |-- serviceId: long (nullable = true)
 |    |-- sessionId: string (nullable = true)
 |    |-- event: string (nullable = true)
 |    |-- targetId: string (nullable = true)
 |    |-- positionX: integer (nullable = true)
 |    |-- positionY: integer (nullable = true)
 |    |-- location: string (nullable = true)
 |    |-- prevLocation: string (nullable = true)
 |    |-- referrer: string (nullable = true)
 |    |-- timestamp: long (nullable = true)
 |    |-- pageDuration: long (nullable = true)
 |-- topic: string (nullable = true)
 |-- partition: integer (nullable = true)
 |-- offset: long (nullable = true)
 |-- timestamp: timestamp (nullable = true)
 |-- timestampType: integer (nullable = true)
```
전처리가 완료된 Dataframe의 스키마는 다음과 같다.
```
root
 |-- key: string (nullable = true)
 |-- service_token: string (nullable = true)
 |-- client_id: long (nullable = true)
 |-- service_id: long (nullable = true)
 |-- session_id: string (nullable = true)
 |-- event: string (nullable = true)
 |-- target_id: string (nullable = true)
 |-- position_x: integer (nullable = true)
 |-- position_y: integer (nullable = true)
 |-- location: string (nullable = true)
 |-- prev_location: string (nullable = true)
 |-- referrer: string (nullable = true)
 |-- creation_timestamp: long (nullable = true)
 |-- page_duration: long (nullable = true)
```
Cassandra에 Keyspace와 Table이 정의되기 전까진 작업을 실행할 수 없다.

## 5. Cassandra
Spark로 전처리 된 데이터를 담기 위해 Keyspace와 Table을 생성한다.
```
> sudo docker attach master01
> cqlsh master01 9042
cqlsh> CREATE KEYSPACE tagmanager WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
cqlsh> USE tagmanager;
cqlsh> CREATE TABLE stream (
    key TEXT,
    service_token TEXT,
    client_id BIGINT,
    service_id BIGINT,
    session_id TEXT,
    event TEXT,
    target_id TEXT,
    position_x INT,
    position_y INT,
    location TEXT,
    prev_location TEXT,
    referrer TEXT,
    creation_timestamp TIMESTAMP,
    page_duration BIGINT,
    PRIMARY KEY ((service_id), creation_timestamp, session_id)
) WITH CLUSTERING ORDER BY (creation_timestamp DESC);
```