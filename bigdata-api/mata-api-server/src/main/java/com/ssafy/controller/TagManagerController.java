package com.ssafy.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.dto.WebLog;
import com.ssafy.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TagManagerController {

    private final KafkaProducerService kafkaProducerService;
    @PostMapping("/dump")
    public ResponseEntity<?> getLogDump(@RequestBody WebLog[] body) {

        List referlist = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            referlist.add("https://www.google.com/" + ("mata"+i).hashCode());
        }
        for (int i = 0; i < 100; i++) {
            referlist.add("https://www.naver.com/" + ("mata"+i).hashCode());
        }
        for (int i = 0; i < 100; i++) {
            referlist.add("https://www.daum.com/" + ("mata"+i).hashCode());
        }

        Arrays.stream(body).forEach(wl -> {
//            kafkaProducerService.checkValidation(wl.getServiceToken()); // 토큰 검증 로직
//            wl.setServiceId(kafkaProducerService.getProjectId(wl.getServiceToken())); // 토큰으로 서비 아이디 가져오기
            System.out.println(wl.getServiceToken());
            System.out.println(wl.getSessionId());
            System.out.println(wl.getEvent());
            System.out.println(wl.getTargetId());
            System.out.println(wl.getPositionX());
            System.out.println(wl.getPositionY());
            System.out.println(wl.getLocation());
            System.out.println(new Timestamp(wl.getTimestamp()));
            System.out.println(wl.getPrevLocation());
            if(wl.getPrevLocation().equals("none")) {
                wl.setReferrer((String) referlist.get((int) (Math.random() * 300)));
            }
            System.out.println(wl.getReferrer());
            try {
                kafkaProducerService.sendToKafka(wl);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
