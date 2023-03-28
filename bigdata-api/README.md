# Springboot to Kafka

### 메시지 전달 후, console consumer로 확인
```
> kafka-console-consumer.sh --topic tagmanager --from-beginning --bootstrap-server localhost:9092
## 출력 예시
{
    "serviceToken":"tag-manager-service-token",
    "sessionId":"test-session-id",
    "event":"click",
    "targetId":"button-to-second",
    "positionX": 369
    "positionY":587,
    "location":"http://localhost:3000/",
    "timestamp":1679200273452
}
```

### cassandra 테이블
```
CREATE TABLE stream (
    key TEXT,
    service_token TEXT,
    client_id BIGINT,
    session_id TEXT,
    event TEXT,
    target_id TEXT,
    position_x INT,
    position_y INT,
    location TEXT,
    creation_timestamp TIMESTAMP,
    PRIMARY KEY ((client_id), creation_timestamp, location, session_id, event)
) WITH CLUSTERING ORDER BY (creation_timestamp DESC);
```

# Build Guide

1. Bulild 수행 시 다음과 같은 명령어를 루트 프로젝트 `S08P22A506\bigdata-api`에서 실행시킵니다.

~~~text
mvn clean install
~~~

2. 다음 `cd .\mata-api-server\target\` 명령어를 통해 target 디렉토리로 이동 후, 다음 명령어를 실행합니다.
~~~text
java -jar .\mata-api-server-0.0.1-SNAPSHOT.jar
~~~

