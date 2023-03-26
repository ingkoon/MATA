package com.ssafy.service;

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
}
