package com.company.backend.didemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiDemoApplication.class, args);

        // Keeping the application alive
        // so that we can view the beans in real time from IntelliJ IDEA
        try {
            Thread.sleep(Long.MAX_VALUE);  // Keeps the app running indefinitely
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
