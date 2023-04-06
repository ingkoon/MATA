package com.ssafy.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.dto.WebLog;
import com.ssafy.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.*;

@RestController
@Slf4j
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
            kafkaProducerService.checkValidation(wl.getServiceToken()); // 토큰 검증 로직
            wl.setServiceId(kafkaProducerService.getProjectId(wl.getServiceToken())); // 토큰으로 서비 아이디 가져오기
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

    @GetMapping("/exampledata_webtojava")
    public ResponseEntity<?> getLogDump() {

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

        List<String> urlList = new ArrayList<>();
        // 10개
        urlList.add("/");
        urlList.add("/first");
        urlList.add("/second");
        urlList.add("/first/abcabc");
        urlList.add("/first/shop");
        urlList.add("/first/list");
        urlList.add("/second/qna");
        urlList.add("/second/board");
        urlList.add("/second/map");
        urlList.add("/journals");

        List<String> idList = new ArrayList<>();
        idList.add("button-back");
        idList.add("button-event");
        idList.add("map1");
        idList.add("map2");

        // 1000명의 유저 접속, url 랜덤
        for (int i = 0; i < 1000; i++) {
            // 10개의 event
            for (int j = 0; j < 10; j++) {
                WebLog wl = new WebLog();
                wl.setServiceId(2L);
                int hashValue = (int)(Math.random()*100000);
                int hashValue2 = (int)(Math.random()*100000);

                wl.setServiceToken("token.........."+i);
                wl.setSessionId(String.valueOf(String.valueOf(hashValue).hashCode()));
                wl.setPrevLocation("none");
                long nowTime = System.currentTimeMillis();
                long time = nowTime - nowTime%1000000;

                // 외부 접속
                if(i%5 == 0) {
                    wl.setPrevLocation("none");
                    wl.setLocation("http://localhost:3001"+urlList.get(i%3));
                } else {
                    // 내부 이동
//                    x = true ? asdf : asdf;
                    wl.setPrevLocation("http://localhost:3001"+urlList.get(hashValue%10));
                    if(hashValue % 10 == hashValue2 % 10) {
                        hashValue2++;
                    }
                    wl.setLocation("http://localhost:3001"+urlList.get(hashValue2%10 ));
                }
                long duTime = 10 + hashValue % 100;
                int hashValue3 = (int)(Math.random()*100000);
                wl.setTimestamp(time+hashValue*10000);
                wl.setEvent("none");
                if(j==0) {
                    // pageenter
                    wl.setEvent("pageenter");
                    wl.setPageDuration(0);
                    wl.setPositionX(0);
                    wl.setPositionY(0);
                } else if(j==9) {
                    // pageleave
                    wl.setEvent("pageleave");
                    wl.setPageDuration(duTime*j);
                    wl.setPositionX(0);
                    wl.setPositionY(0);
                } else {
                    // click
                    wl.setTargetId(idList.get(hashValue3%4));
                    wl.setEvent("click");
                    wl.setPageDuration(duTime*j);
                    wl.setPositionX(hashValue%1000 + hashValue3%10);
                    wl.setPositionY(hashValue%520 + hashValue3%10);
                }

                if(wl.getPrevLocation().equals("none")) {
                    // none -> 특정 주소로 변경...
                    wl.setReferrer((String) referlist.get((int) (Math.random() * 300)));
                }
                try {
                    kafkaProducerService.sendToKafka(wl);
                    log.info("input" + i +" "+ j);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
