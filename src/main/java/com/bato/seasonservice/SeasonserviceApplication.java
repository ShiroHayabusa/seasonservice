package com.bato.seasonservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class SeasonserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeasonserviceApplication.class, args);
    }

}
