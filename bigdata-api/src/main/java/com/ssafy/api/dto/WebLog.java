package com.ssafy.api.dto;

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
    private String sessionId;
    private String event;
    private String targetId;
    private MousePosition position;
    private String location;
    private long timestamp;

    public ProducerRecord<String, String> toProducerRecord(String topic, Integer partition) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return new ProducerRecord<>(topic, partition, this.timestamp, this.sessionId+"-"+this.timestamp, mapper.writeValueAsString(this));
    }

}
