package com.ssafy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class MataApiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MataApiServerApplication.class, args);
    }
}
