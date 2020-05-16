package com.eroom.erooja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EroojaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EroojaApplication.class, args);
    }

}