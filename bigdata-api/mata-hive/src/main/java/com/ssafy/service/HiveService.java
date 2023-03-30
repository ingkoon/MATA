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

    public List<Component> getComponents(long baseTime, String interval){
        return hiveRepository.selectComponent(baseTime, interval);
    }

    public List<Click> getClicks(long baseTime, String interval){
        return hiveRepository.selectClick(baseTime, interval);
    }
    public List<PageDuration> getPageDurations(long baseTime, String interval){
        return hiveRepository.selectPageDuration(baseTime, interval);
    }
    public List<PageJournal> getPageJournals(String baseTime, String interval){
        return hiveRepository.selectPageJournal(baseTime, interval);
    }
    public List<PageRefer> getPageRefers(long baseTime, String interval){
        return hiveRepository.selectpageRefer(baseTime, interval);
    }
    public List<Referrer> getReferrers(){
        return hiveRepository.selectReferrer();
    }

}
