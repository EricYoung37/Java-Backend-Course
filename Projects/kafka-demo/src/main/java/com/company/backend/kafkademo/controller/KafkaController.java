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
    public String publishMessage(@RequestParam("key") String key, @RequestParam("message") String message) {
        kafkaProducerServiceAtLeastOnce.sendMessage(key, "[ALO] " + message);
        kafkaProducerServiceAtMostOnce.sendMessage(key, "[AMO] " + message);
        kafkaProducerServiceExactlyOnce.sendMessage(key, "[EO] " + message);
        return "Message published successfully";
    }
}
