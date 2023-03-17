package com.ssafy.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.api.dto.WebLog;
import com.ssafy.api.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TagManagerController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/dump")
    public ResponseEntity<?> getLogDump(@RequestBody WebLog[] body) {
        Arrays.stream(body).forEach(wl -> {
            System.out.println(wl.getServiceToken());
            System.out.println(wl.getSessionId());
            System.out.println(wl.getEvent());
            System.out.println(wl.getTargetId());
            System.out.println(wl.getPosition().getPageX() + ", " + wl.getPosition().getPageY());
            System.out.println(wl.getLocation());
            System.out.println(new Timestamp(wl.getTimestamp()));
            try {
                kafkaProducerService.sendToKafka(wl);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
