package com.ssafy.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class HiveConfiguration {

    @Value("${hive.driver-class-name}")
    private String driverClassName;
    @Value("${hive.url}")
    private String hiveUrl;

    @Value("${hive.username}")
    private String hiveUsername;

    @Value("${hive.password}")
    private String hivePassword;

    @Bean
    public DataSource hiveDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(hiveUrl);
        dataSource.setUsername(hiveUsername);
        dataSource.setPassword(hivePassword);
        return dataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate hiveJdbcTemplate() {
        return new JdbcTemplate(hiveDataSource());
    }
}
