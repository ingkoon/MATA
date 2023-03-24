#!/usr/bin/env bash

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
) WITH CLUSTERING ORDER BY (creation_timestamp DESC);"
sudo docker exec master01 hive -e "
CREATE DATABASE IF NOT EXISTS mata;
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
  CONSTRAINT fk_components_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journal(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journal_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.referrers(
  referrer_id BIGINT,
  referrer_name STRING,
  PRIMARY KEY(referrer_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers(
  total_session INT,
  total_pageenter STRING,
  update_timestamp TIMESTAMP,
  referrer_id BIGINT,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_referrer_id FOREIGN KEY(referrer_id) REFERENCES mata.referrers(referrer_id) DISABLE,
  CONSTRAINT fk_page_refers_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE 
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
"
