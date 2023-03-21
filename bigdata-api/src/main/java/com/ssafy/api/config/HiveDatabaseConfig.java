package com.ssafy.api.config;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.jta.JtaTransactionManager;
@Configuration
@EnableTransactionManagement
public class HiveDatabaseConfig implements TransactionManagementConfigurer {

    @Value("${hive.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${hive.datasource.url}")
    private String url;

    @Value("${hive.datasource.username}")
    private String username;

    @Value("${hive.datasource.password}")
    private String password;

    @Bean
    public DataSource hiveDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean hiveEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setDataSource(hiveDataSource());
        entityManagerFactoryBean.setPackagesToScan("com.example.demo.domain.hive");
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager hiveTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(hiveEntityManagerFactory(builder).getObject());
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return hiveTransactionManager(null);
    }
}