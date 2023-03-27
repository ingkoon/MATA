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

    public List<Component> getComponents(){
        return hiveRepository.selectComponent();
    }

    public List<Click> getClicks(){
        return hiveRepository.selectClick();
    }
    public List<PageDuration> getPageDurations(){
        return hiveRepository.selectPageDuration();
    }
    public List<PageJournal> getPageJournals(){
        return hiveRepository.selectPageJournal();
    }
    public List<PageRefer> getPageRefers(){
        return hiveRepository.selectpageRefer();
    }
    public List<Referrer> getReferrers(){
        return hiveRepository.selectReferrer();
    }

}
