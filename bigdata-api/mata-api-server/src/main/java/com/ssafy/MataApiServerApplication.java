package com.ssafy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EntityScan(basePackages = {"com.ssafy"})
//@EnableJpaRepositories(basePackages = {"com.ssafy"})
@EnableWebMvc
public class MataApiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MataApiServerApplication.class, args);
    }
}
