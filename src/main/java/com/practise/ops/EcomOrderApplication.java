package com.practise.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableCaching
public class EcomOrderApplication {
    public static void main(String[] args) {

        SpringApplication.run(EcomOrderApplication.class, args);
    }
}