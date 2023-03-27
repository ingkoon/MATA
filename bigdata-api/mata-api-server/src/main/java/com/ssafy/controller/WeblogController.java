package com.ssafy.controller;

import com.ssafy.entity.*;
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
    @GetMapping("/components")
    public ResponseEntity<List<Component>> getComponents(@AuthenticationPrincipal UserDetails userDetails){
        List<Component> components = hiveService.getComponents();
        return ResponseEntity.status(HttpStatus.OK).body(components);
    }
    @GetMapping("/clicks")
    public ResponseEntity<List<Click>> getClicks(@AuthenticationPrincipal UserDetails userDetails){
        List<Click> clicks = hiveService.getClicks();
        return ResponseEntity.status(HttpStatus.OK).body(clicks);
    }
    @GetMapping("/durations")
    public ResponseEntity<List<PageDuration>> getPageDurations(@AuthenticationPrincipal UserDetails userDetails){
        List<PageDuration> pageDurations = hiveService.getPageDurations();
        return ResponseEntity.status(HttpStatus.OK).body(pageDurations);
    }
    @GetMapping("/journals")
    public ResponseEntity<List<PageJournal>> getPageJournals(@AuthenticationPrincipal UserDetails userDetails){
        List<PageJournal> pageJournals = hiveService.getPageJournals();
        return ResponseEntity.status(HttpStatus.OK).body(pageJournals);
    }
    @GetMapping("/refers")
    public ResponseEntity<List<PageRefer>> getPageRefers(@AuthenticationPrincipal UserDetails userDetails){
        List<PageRefer> pageRefers = hiveService.getPageRefers();
        return ResponseEntity.status(HttpStatus.OK).body(pageRefers);
    }
    @GetMapping("/referrers")
    public ResponseEntity<List<Referrer>> getReferrers(@AuthenticationPrincipal UserDetails userDetails){
        List<Referrer> referrers = hiveService.getReferrers();
        return ResponseEntity.status(HttpStatus.OK).body(referrers);
    }
}
