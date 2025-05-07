package com.company.backend.kafkademo.controller;

import com.company.backend.kafkademo.service.KafkaProducerServiceAtLeastOnce;
import com.company.backend.kafkademo.service.KafkaProducerServiceAtMostOnce;
import com.company.backend.kafkademo.service.KafkaProducerServiceExactlyOnce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    @Autowired
    private KafkaProducerServiceAtLeastOnce kafkaProducerServiceAtLeastOnce;

    @Autowired
    private KafkaProducerServiceAtMostOnce kafkaProducerServiceAtMostOnce;

    @Autowired
    private KafkaProducerServiceExactlyOnce kafkaProducerServiceExactlyOnce;

    // only one endpoint on purpose to demonstrate different handling approaches within inside Kafka
    @PostMapping("/publish")
    public String publishMessage(@RequestParam("key") String key,
                                 @RequestParam("message") String message) {
        kafkaProducerServiceAtLeastOnce.sendMessage(key, "[ALO] " + message);
        kafkaProducerServiceAtMostOnce.sendMessage(key, "[AMO] " + message);
        kafkaProducerServiceExactlyOnce.sendMessage(key, "[EO] " + message);
        return "Message published successfully";
    }

    // Test endpoint for at-least-once
    @PostMapping("/test-at-least-once")
    public String testAtLeastOnce(@RequestParam("key") String key,
                                  @RequestParam("message") String message) {
        kafkaProducerServiceAtLeastOnce.sendMessage(key, "[ALO-TEST] " + message);
        return "At-least-once test message published";
    }

    // Test endpoint for at-most-once
    @PostMapping("/test-at-most-once")
    public String testAtMostOnce(@RequestParam("key") String key,
                                 @RequestParam("message") String message) {
        kafkaProducerServiceAtMostOnce.sendMessage(key, "[AMO-TEST] " + message);
        return "At-most-once test message published";
    }

    // Test endpoint for exactly-once
    @PostMapping("/test-exactly-once")
    public String testExactlyOnce(@RequestParam("key") String key,
                                  @RequestParam("message") String message) {
        kafkaProducerServiceExactlyOnce.sendMessage(key, "[EO-TEST] " + message);
        return "Exactly-once test message published";
    }
}
