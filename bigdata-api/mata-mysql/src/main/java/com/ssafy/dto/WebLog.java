package com.ssafy.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.w3c.dom.events.EventTarget;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebLog {

    private String serviceToken;
    private long clientId;
    private long serviceId;
    private String sessionId;
    private String event;
    private String targetId;
    private int positionX;
    private int positionY;
    private String location;
    private String prevLocation;
    private String referrer;
    private long timestamp;
    private long pageDuration;

    public ProducerRecord<String, String> toProducerRecord(String topic, Integer partition) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.clientId = 1L;
        this.serviceId = 2L;
        return new ProducerRecord<>(topic, partition, this.timestamp, this.sessionId+"-"+this.timestamp, mapper.writeValueAsString(this));
    }

}
