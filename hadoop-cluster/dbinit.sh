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
  page_duration BIGINT,
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
CREATE TABLE IF NOT EXISTS mata.components_1m(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_1m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_1m(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_1m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_1m(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_1m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_1m(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_1m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_1m(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_1m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_5m(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_5m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_5m(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_5m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_5m(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_5m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_5m(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_5m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_5m(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_5m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_10m(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_10m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_10m(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_10m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_10m(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_10m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_10m(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_10m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_10m(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_10m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_30m(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_30m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_30m(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_30m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_30m(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_30m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_30m(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_30m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_30m(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_30m_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_1H(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_1H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_1H(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_1H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_1H(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_1H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_1H(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_1H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_1H(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_1H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_6H(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_6H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_6H(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_6H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_6H(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_6H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_6H(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_6H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_6H(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_6H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_12H(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_12H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_12H(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_12H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_12H(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_12H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_12H(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_12H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_12H(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_12H_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_1d(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_1d_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_1d(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_1d_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_1d(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_1d_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_1d(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_1d_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_1d(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_1d_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_1w(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_1w_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_1w(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_1w_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_1w(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_1w_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_1w(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_1w_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_1w(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_1w_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_1mo(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_1mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_1mo(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_1mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_1mo(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_1mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_1mo(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_1mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_1mo(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_1mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_6mo(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_6mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_6mo(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_6mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_6mo(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_6mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_6mo(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_6mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_6mo(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_6mo_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_1y(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_1y_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_1y(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_1y_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_1y(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_1y_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_1y(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_1y_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_1y(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_1y_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;

CREATE DATABASE IF NOT EXISTS mata;
USE mata;
CREATE TABLE IF NOT EXISTS services(
  service_id BIGINT,
  host_name STRING,
  PRIMARY KEY(service_id) DISABLE NOVALIDATE
) STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.components_all(
  total_click INT,
  target_id STRING,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_components_all_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.clicks_all(
  total_click INT,
  position_x INT,
  position_y INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_clicks_all_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_durations_all(
  total_duration BIGINT,
  total_session INT,
  location STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_durations_all_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_journals_all(
  total_journal INT,
  location_from STRING,
  location_to STRING,
  update_timestamp TIMESTAMP,
  service_id BIGINT,
  CONSTRAINT fk_page_journals_all_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
CREATE TABLE IF NOT EXISTS mata.page_refers_all(
  total_session INT,
  total_pageenter BIGINT,
  update_timestamp TIMESTAMP,
  referrer STRING,
  service_id BIGINT,
  CONSTRAINT fk_page_refers_all_service_id FOREIGN KEY(service_id) REFERENCES mata.services(service_id) DISABLE NOVALIDATE
) CLUSTERED BY (service_id) SORTED BY (update_timestamp DESC) INTO 10 BUCKETS
STORED AS ORC;
"
