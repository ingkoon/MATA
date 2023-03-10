package com.ssafy.api.controller;

import com.ssafy.api.dto.WebLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class TagManagerController {

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
        });
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
