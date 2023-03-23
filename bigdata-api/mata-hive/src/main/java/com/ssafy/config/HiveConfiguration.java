package com.ssafy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
public class HiveConfiguration {
    @Value("${hive.url}")
    private String hiveUrl;

    @Value("${hive.username}")
    private String hiveUsername;

    @Value("${hive.password}")
    private String hivePassword;

    @Bean
    public DataSource hiveDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
        dataSource.setUrl(hiveUrl);
        dataSource.setUsername(hiveUsername);
        dataSource.setPassword(hivePassword);
        return dataSource;
    }

    @Bean
    public JdbcTemplate hiveJdbcTemplate() {
        return new JdbcTemplate(hiveDataSource());
    }
}
