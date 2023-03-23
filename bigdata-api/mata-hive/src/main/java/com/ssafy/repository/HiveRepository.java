package com.ssafy.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HiveRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> selectData() {
        String sql = "show databases";
        return jdbcTemplate.queryForList(sql);
    }
}
