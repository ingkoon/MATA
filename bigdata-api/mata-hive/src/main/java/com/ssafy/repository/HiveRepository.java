package com.ssafy.repository;


import com.ssafy.entity.Weblogs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HiveRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> selectData() {
        String sql = "SELECT * FROM weblogs";
        return jdbcTemplate.queryForList(sql);
    }
}
