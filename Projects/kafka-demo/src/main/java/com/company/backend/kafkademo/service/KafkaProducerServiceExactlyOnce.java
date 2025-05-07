package com.company.backend.kafkademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceExactlyOnce extends AbstractKafkaProducerService {

    @Value("${kafka.topic.exactly-once}")
    private String topic;

    @Autowired
    @Qualifier("exactlyOnceKafkaTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String key, String message) {
        kafkaTemplate.executeInTransaction(kt -> {
            // Simulate random failures (30% chance)
            if (Math.random() < 0.3) {
                throw new RuntimeException("Simulated transaction failure");
            }

            kt.send(topic, key, message);
            return true;
        });
    }
}
