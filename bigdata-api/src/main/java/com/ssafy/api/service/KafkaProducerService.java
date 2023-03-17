package com.ssafy.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.api.dto.WebLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final NewTopic topic;
    private final KafkaTemplate<String, String> template;

    public void sendToKafka(final WebLog data) throws JsonProcessingException {
        final ProducerRecord<String, String> record = data.toProducerRecord(topic.name(), 1);
        ListenableFuture<SendResult<String, String>> future = template.send(record);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Failed to send data: "+data);
                log.warn("Failed to send data: "+data, ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Data sent successfully: "+data);
                log.warn("Data sent successfully: "+data);
            }
        });
    }

}
