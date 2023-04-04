package com.ssafy.repository;


import com.ssafy.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HiveRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Component> componentRowMapper = (resultSet, rowNum) -> {
        Component component = new Component();
        component.setTotalClick(resultSet.getInt("total_click"));
        component.setTargetId(resultSet.getString("target_id"));
        component.setLocation(resultSet.getString("location"));
        component.setUpdateTimestamp(resultSet.getTimestamp("update_timestamp"));
        component.setServiceId(resultSet.getLong("service_id"));
        return component;
    };
    private final RowMapper<Click> clickRowMapper = (resultSet, rowNum) -> {
        Click click = new Click();
        click.setTotalClick(resultSet.getInt("total_click"));
        click.setPositionX(resultSet.getInt("position_x"));
        click.setPositionY(resultSet.getInt("position_y"));
        click.setLocation(resultSet.getString("location"));
        click.setUpdateTimestamp(resultSet.getTimestamp("update_timestamp"));
        click.setServiceId(resultSet.getLong("service_id"));
        return click;
    };
    private final RowMapper<PageDuration> pageDurationRowMapper = (resultSet, rowNum) -> {
        PageDuration pageDuration = new PageDuration();
        pageDuration.setTotalDuration(resultSet.getLong("total_duration"));
        pageDuration.setTotalSession(resultSet.getInt("total_session"));
        pageDuration.setLocation(resultSet.getString("location"));
        pageDuration.setUpdateTimestamp(resultSet.getTimestamp("update_timestamp"));
        pageDuration.setServiceId(resultSet.getLong("service_id"));
        return pageDuration;
    };
    private final RowMapper<PageJournal> pageJournalRowMapper = (resultSet, rowNum) -> {
        PageJournal pageJournal = new PageJournal();
        pageJournal.setTotalJournal(resultSet.getInt("total_journal"));
        pageJournal.setLocationFrom(resultSet.getString("location_from"));
        pageJournal.setLocationTo(resultSet.getString("location_to"));
        pageJournal.setUpdateTimestamp(resultSet.getTimestamp("update_timestamp"));
        pageJournal.setServiceId(resultSet.getLong("service_id"));
        return pageJournal;
    };
    private final RowMapper<PageRefer> pageReferRowMapper = (resultSet, rowNum) -> {
        PageRefer pageRefer = new PageRefer();
        pageRefer.setTotalSession(resultSet.getInt("total_session"));
        pageRefer.setTotalPageenter(resultSet.getLong("total_pageenter"));
        pageRefer.setUpdateTimestamp(resultSet.getTimestamp("update_timestamp"));
        pageRefer.setReferrer(resultSet.getString("referrer"));
        pageRefer.setServiceId(resultSet.getLong("service_id"));
        return pageRefer;
    };

    public List<Map<String, Object>> selectData() {
        String sql = "SHOW DATABASES;";
        return jdbcTemplate.queryForList(sql);
    }
    public List<Component> selectComponent(long baseTime, String interval, long serviceId) {
        String sql = String.format(//language=sql
                "SELECT * FROM mata.components_%s "+
                    "WHERE service_id=%d "+
                        "AND update_timestamp<CAST(%d AS TIMESTAMP) "+
                    "LIMIT 100", interval, serviceId, baseTime);
        return jdbcTemplate.query(sql, componentRowMapper);
    }
    public List<Click> selectClick(long baseTime, String interval, long serviceId, String location) {
        String sql = String.format(//language=sql
                "SELECT * FROM mata.clicks_%s "+
                    "WHERE service_id=%d "+
                        "AND update_timestamp<CAST(%d AS TIMESTAMP) AND location = %s"+
                    "LIMIT 100", interval, serviceId, baseTime, location);
        return jdbcTemplate.query(sql, clickRowMapper);
    }
    public List<PageDuration> selectPageDuration(long baseTime, String interval, long serviceId) {
        String sql = String.format(//language=sql
                "SELECT * FROM mata.page_durations_%s "+
                    "WHERE service_id=%d "+
                        "AND update_timestamp<CAST(%d AS TIMESTAMP) "+
                    "LIMIT 100", interval, serviceId, baseTime);
        return jdbcTemplate.query(sql, pageDurationRowMapper);
    }
    public List<PageJournal> selectPageJournal(long baseTime, String interval, long serviceId) {
        String sql = String.format(//language=sql
                "SELECT * FROM mata.page_journals_%s "+
                    "WHERE service_id=%d "+
                        "AND update_timestamp BETWEEN CAST(%d AS TIMESTAMP) "+
                        "AND CAST(%d AS TIMESTAMP) ", interval, serviceId, baseTime-86400000, baseTime);
        return jdbcTemplate.query(sql, pageJournalRowMapper);
    }
    public List<PageRefer> selectPageRefer(long baseTime, String interval, long serviceId) {
        String sql = String.format(//language=sql
                "SELECT * FROM mata.page_refers_%s "+
                "WHERE service_id=%d "+
                    "AND update_timestamp<CAST(%d AS TIMESTAMP) "+
                "LIMIT 100", interval, serviceId, baseTime);
        return jdbcTemplate.query(sql, pageReferRowMapper);
    }

    public List<PageRefer> selectPageReferAll(long baseTime, String interval, long serviceId) {
        String sql = String.format(//language=sql
                "SELECT * FROM mata.page_refers_%s "+
                        "WHERE service_id=%d "+
                        "AND update_timestamp BETWEEN CAST(%d AS TIMESTAMP) "+
                        "AND CAST(%d AS TIMESTAMP) ", interval, serviceId, baseTime-86400000, baseTime);
        return jdbcTemplate.query(sql, pageReferRowMapper);
    }

    public List<PageDuration> selectPageUser(long baseTime, String interval, long serviceId) {
        String sql = String.format(//language=sql
                "SELECT * FROM mata.page_durations_%s "+
                    "WHERE service_id=%d "+
                        "AND update_timestamp<CAST(%d AS TIMESTAMP) "+
                    "LIMIT 100", interval, serviceId, baseTime);
        return jdbcTemplate.query(sql, pageDurationRowMapper);
    }
}
