package com.example.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SchedulerServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(SchedulerServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SchedulerServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println(" Scheduler Service Started");

    }
}