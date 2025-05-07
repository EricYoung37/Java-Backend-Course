package com.company.backend.kafkademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceAtMostOnce extends AbstractKafkaProducerService {

    @Value("${kafka.topic.at-most-once}")
    private String topic;

    @Autowired
    @Qualifier("atMostOnceKafkaTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String key, String message) {
        try {
            // Simulate random failures (30% chance)
            if (Math.random() < 0.3) {
                throw new RuntimeException("Simulated producer failure after send");
            }

            kafkaTemplate.send(topic, key, message).get(); // .get() makes it blocking;
        } catch (Exception e) {
            System.err.println("Failed to send message (won't retry): " + message);
            e.printStackTrace();
        }
    }
}
