package com.raven.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RavenApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RavenApiApplication.class, args);
    }

}
