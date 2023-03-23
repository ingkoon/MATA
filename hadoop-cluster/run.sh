#!/usr/bin/env bash

sudo docker-compose down && \
\
sudo docker build -t cluster:node -f node/Dockerfile . && \
sudo docker build -t cluster:client -f client/Dockerfile . && \
sudo docker-compose up -d && \
\
sudo bash sbin/deploy-ssh-keys.sh && \
sudo bash sbin/deploy-ssh-authorized-keys.sh && \
\
sudo docker exec -u root metastore mysql -u root -proot -e "
  CREATE DATABASE hive;
  CREATE DATABASE airflow;
  CREATE DATABASE mata;
  CREATE USER hive IDENTIFIED BY 'hive';
  CREATE USER airflow IDENTIFIED BY 'airflow';
  CREATE USER spring IDENTIFIED BY 'spring';
  GRANT ALL PRIVILEGES ON hive.* TO 'hive'@'%';
  GRANT ALL PRIVILEGES ON mata.* TO 'spring'@'%';
  GRANT ALL PRIVILEGES ON airflow.* TO 'airflow'@'%';"
\
sudo docker exec slave01 rabbitmq-plugins enable rabbitmq_management && \
sudo docker exec slave01 /usr/sbin/rabbitmq-server start -detached && \
sleep 10 && \
sudo docker exec slave01 rabbitmqctl add_user airflow-user airflow-user && \
sudo docker exec slave01 rabbitmqctl add_vhost airflow && \
sudo docker exec slave01 rabbitmqctl set_user_tags airflow-user administrator && \
sudo docker exec slave01 rabbitmqctl set_permissions -p airflow airflow-user ".*" ".*" ".*" && \
\
sudo docker exec master01 airflow db init && \
sudo docker exec master01 airflow users create --username admin  --password admin \
  --firstname FIRST_NAME --lastname LAST_NAME --role Admin --email admin@example.org && \
sudo docker exec -d master01 sh -c "airflow celery worker > /usr/local/lib/apache-airflow-2.5.0/logs/master01-celery-worker.log" && \
sudo docker exec -d master02 sh -c "airflow celery worker > /usr/local/lib/apache-airflow-2.5.0/logs/master02-celery-worker.log" && \
sudo docker exec -d slave01 sh -c "airflow celery worker > /usr/local/lib/apache-airflow-2.5.0/logs/slave01-celery-worker.log" && \
sudo docker exec -d slave02 sh -c "airflow celery worker > /usr/local/lib/apache-airflow-2.5.0/logs/slave02-celery-worker.log" && \
sudo docker exec -d slave03 sh -c "airflow celery worker > /usr/local/lib/apache-airflow-2.5.0/logs/slave03-celery-worker.log" && \
sudo docker exec -d slave01 sh -c "airflow celery flower > /usr/local/lib/apache-airflow-2.5.0/logs/slave01-celery-flower.log" && \
sudo docker exec -d slave01 sh -c "airflow scheduler > /usr/local/lib/apache-airflow-2.5.0/logs/slave01-scheduler.log" && \
sudo docker exec -d slave01 sh -c "airflow webserver --port 5080 > /usr/local/lib/apache-airflow-2.5.0/logs/slave01-web-server.log" && \
\
sudo bash lib/apache-zookeeper-3.7.1-bin/sbin/deploy-myid.sh && \
sudo docker exec master01 sh -c "zkServer.sh start" && \
sudo docker exec master02 sh -c "zkServer.sh start" && \
sudo docker exec slave01 sh -c "zkServer.sh start" && \
\
export KAFKA_HOME=/usr/local/lib/kafka_2.12-3.4.0 && \
sudo bash lib/kafka_2.12-3.4.0/sbin/deploy-brokerid.sh && \
sudo docker exec -d master01 kafka-server-start.sh $KAFKA_HOME/config/server.properties && \
sudo docker exec -d master02 kafka-server-start.sh $KAFKA_HOME/config/server.properties && \
sudo docker exec -d slave01 kafka-server-start.sh $KAFKA_HOME/config/server.properties && \
sudo docker exec -d slave02 kafka-server-start.sh $KAFKA_HOME/config/server.properties && \
sudo docker exec -d slave03 kafka-server-start.sh $KAFKA_HOME/config/server.properties && \
\
sudo docker exec master01 sh -c "hdfs zkfc -formatZK" && \
sudo docker exec master01 sh -c "hdfs --daemon start journalnode" && \
sudo docker exec master02 sh -c "hdfs --daemon start journalnode" && \
sudo docker exec slave01 sh -c "hdfs --daemon start journalnode" && \
\
sudo docker exec master01 sh -c "hdfs namenode -format" && \
sudo docker exec master01 sh -c "start-dfs.sh" && \
sudo docker exec master02 sh -c "hdfs namenode -bootstrapStandby" && \
sudo docker exec master02 sh -c "hdfs --daemon start namenode" && \
\
sudo docker exec master01 sh -c "start-yarn.sh" && \
sudo docker exec master01 sh -c "mapred --daemon start historyserver" && \
sudo docker exec master02 sh -c "mapred --daemon start historyserver" && \
\
sudo docker exec master01 schematool -initSchema -dbType mysql && \
#\
#export FLUME_CONF_DIR=/usr/local/lib/apache-flume-1.11.0-bin/conf && \
#export FLUME_LOG_DIR=/usr/local/lib/apache-flume-1.11.0-bin && \
#sudo docker exec -d master01 flume-ng agent -c $FLUME_CONF_DIR -f $FLUME_CONF_DIR/flume-hdfs-conf.properties -Dflume.log.dir=$FLUME_LOG_DIR -n hdfs-airflow-log && \
#sudo docker exec -d master02 flume-ng agent -c $FLUME_CONF_DIR -f $FLUME_CONF_DIR/flume-hdfs-conf.properties -Dflume.log.dir=$FLUME_LOG_DIR -n hdfs-airflow-log && \
#sudo docker exec -d slave01 flume-ng agent -c $FLUME_CONF_DIR -f $FLUME_CONF_DIR/flume-hdfs-conf.properties -Dflume.log.dir=$FLUME_LOG_DIR -n hdfs-airflow-log && \
#sudo docker exec -d slave02 flume-ng agent -c $FLUME_CONF_DIR -f $FLUME_CONF_DIR/flume-hdfs-conf.properties -Dflume.log.dir=$FLUME_LOG_DIR -n hdfs-airflow-log && \
#sudo docker exec -d slave03 flume-ng agent -c $FLUME_CONF_DIR -f $FLUME_CONF_DIR/flume-hdfs-conf.properties -Dflume.log.dir=$FLUME_LOG_DIR -n hdfs-airflow-log && \
\
sudo docker exec -d master01 sh -c 'cassandra -R >> $CASSANDRA_HOME/cassandra-startup.log' && \
sleep 10 && \
sudo docker exec -d master02 sh -c 'cassandra -R >> $CASSANDRA_HOME/cassandra-startup.log' && \
sleep 10 && \
sudo docker exec -d slave01 sh -c 'cassandra -R >> $CASSANDRA_HOME/cassandra-startup.log' && \
\
sudo docker exec master02 kafka-topics.sh --create --topic tagmanager --replication-factor 1 --partitions 1 --bootstrap-server localhost:9092
\
sleep 10 && \
sudo docker exec master01 cqlsh master01 9042 -e "
CREATE KEYSPACE tagmanager WITH REPLICATION = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};
USE tagmanager;
CREATE TABLE stream (
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
  age_duration BIGINT,
  PRIMARY KEY ((service_id), creation_timestamp, session_id)
) WITH CLUSTERING ORDER BY (creation_timestamp DESC);
" && \
sudo docker exec master01 hive -e "
CREATE DATABASE mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 32 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 32 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 32 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 32 BUCKETS
STORED AS ORC;
"

