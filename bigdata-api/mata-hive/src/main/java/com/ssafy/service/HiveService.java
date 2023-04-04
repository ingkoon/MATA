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

    public List<Component> getComponents(long baseTime, String interval, long serviceId){
        return hiveRepository.selectComponent(baseTime, interval, serviceId);
    }

    public List<Click> getClicks(long baseTime, String interval, long serviceId, String location){
        return hiveRepository.selectClick(baseTime, interval, serviceId, location);
    }
    public List<PageDuration> getPageDurations(long baseTime, String interval, long serviceId){
        return hiveRepository.selectPageDuration(baseTime, interval, serviceId);
    }

    public List<PageDuration> getPageUsers(long baseTime, String interval, long serviceId){
        return hiveRepository.selectPageUser(baseTime, interval, serviceId);
    }


    public List<PageJournal> getPageJournals(long baseTime, String interval, long serviceId){
        return hiveRepository.selectPageJournal(baseTime, interval, serviceId);
    }
    public List<PageRefer> getPageRefers(long baseTime, String interval, long serviceId){
        return hiveRepository.selectpageRefer(baseTime, interval, serviceId);
    }
    public List<Referrer> getReferrers(){
        return hiveRepository.selectReferrer();
    }

}
