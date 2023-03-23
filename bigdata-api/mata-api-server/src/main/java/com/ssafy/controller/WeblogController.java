package com.ssafy.controller;

import com.ssafy.service.HiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weblog")
public class WeblogController {

    private final HiveService hiveService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getTest(@AuthenticationPrincipal UserDetails userDetails){
        List<Map<String, Object>> webLogs = hiveService.getWebLogs();
        return ResponseEntity.status(HttpStatus.OK).body(webLogs);
    }
}
