package com.ssafy.service;

import com.ssafy.entity.*;
import com.ssafy.repository.HiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HiveService {
    private final HiveRepository hiveRepository;

    public List<Map<String, Object>> getWebLogs(){
        return hiveRepository.selectData();
    }

    public List<Component> getComponents(String baseTime, String interval, long serviceId){
        return hiveRepository.selectComponent(baseTime, interval, serviceId);
    }

    public List<Click> getClicks(String baseTime, String interval, long serviceId){
        return hiveRepository.selectClick(baseTime, interval, serviceId);
    }
    public List<PageDuration> getPageDurations(String baseTime, String interval, long serviceId){
        return hiveRepository.selectPageDuration(baseTime, interval, serviceId);
    }

    public List<PageDuration> getPageUsers(String baseTime, String interval, long serviceId){
        return hiveRepository.selectPageUser(baseTime, interval, serviceId);
    }


    public List<PageJournal> getPageJournals(String baseTime, String interval, long serviceId){
        return hiveRepository.selectPageJournal(baseTime, interval, serviceId);
    }
    public List<PageRefer> getPageRefers(String baseTime, String interval, long serviceId){
        return hiveRepository.selectpageRefer(baseTime, interval, serviceId);
    }
    public List<Referrer> getReferrers(){
        return hiveRepository.selectReferrer();
    }

}
