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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weblog")
public class WeblogController {
    private final HiveService hiveService;
    private final List<String> validation = Arrays.asList("1m", "5m", "10m", "30m", "1h", "6h", "12h", "1d", "1w", "1mo", "6mo", "1y");
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getTest(@AuthenticationPrincipal UserDetails userDetails){
        List<Map<String, Object>> webLogs = hiveService.getWebLogs();
        return ResponseEntity.status(HttpStatus.OK).body(webLogs);
    }
    @GetMapping("/components")
    public ResponseEntity<?> getComponents(@RequestParam(name="basetime") long baseTime,
                                           @RequestParam(name="interval") String interval,
                                           @RequestParam(name="serviceid") long serviceId,
                                           @AuthenticationPrincipal UserDetails userDetails){
        if(!validation.contains(interval)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<Component> components = hiveService.getComponents(baseTime, interval, serviceId);
        return ResponseEntity.status(HttpStatus.OK).body(components);
    }
    @GetMapping("/clicks")
    public ResponseEntity<List<Click>> getClicks(@RequestParam(name="basetime") long baseTime,
                                                 @RequestParam(name="interval") String interval,
                                                 @RequestParam(name="serviceid") long serviceId,
                                                 @RequestParam(name="location") String location,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        if(!validation.contains(interval)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<Click> clicks = hiveService.getClicks(baseTime, interval, serviceId, location);
        return ResponseEntity.status(HttpStatus.OK).body(clicks);
    }
    @GetMapping("/durations")
    public ResponseEntity<List<PageDuration>> getPageDurations(@RequestParam(name="basetime") long baseTime,
                                                               @RequestParam(name="interval") String interval,
                                                               @RequestParam(name="serviceid") long serviceId,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        if(!validation.contains(interval)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<PageDuration> pageDurations = hiveService.getPageDurations(baseTime, interval, serviceId);
        return ResponseEntity.status(HttpStatus.OK).body(pageDurations);
    }

    @GetMapping("/users")
    public ResponseEntity<List<PageDuration>> getPageUsers(@RequestParam(name="basetime") long baseTime,
                                                           @RequestParam(name="interval") String interval,
                                                           @RequestParam(name="serviceid") long serviceId,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        if(!validation.contains(interval)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<PageDuration> pageDurations = hiveService.getPageUsers(baseTime, interval, serviceId);
        return ResponseEntity.status(HttpStatus.OK).body(pageDurations);
    }

    @GetMapping("/journals")
    public ResponseEntity<List<PageJournal>> getPageJournals(@RequestParam(name="basetime") long baseTime,
                                                             @RequestParam(name="interval") String interval,
                                                             @RequestParam(name="serviceid") long serviceId,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        if(!validation.contains(interval)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<PageJournal> pageJournals = hiveService.getPageJournals(baseTime, interval, serviceId);
        return ResponseEntity.status(HttpStatus.OK).body(pageJournals);
    }
    @GetMapping("/refers")
    public ResponseEntity<List<PageRefer>> getPageRefers(@RequestParam(name="basetime") long baseTime,
                                                         @RequestParam(name="interval") String interval,
                                                         @RequestParam(name="serviceid") long serviceId,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        if(!validation.contains(interval)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<PageRefer> pageRefers = hiveService.getPageRefers(baseTime, interval, serviceId);
        return ResponseEntity.status(HttpStatus.OK).body(pageRefers);
    }
    @GetMapping("/referrers")
    public ResponseEntity<List<Referrer>> getReferrers(@AuthenticationPrincipal UserDetails userDetails){
        List<Referrer> referrers = hiveService.getReferrers();
        return ResponseEntity.status(HttpStatus.OK).body(referrers);
    }
}
