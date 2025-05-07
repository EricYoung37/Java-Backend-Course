package com.company.backend.kafkademo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceAtLeastOnce extends AbstractKafkaConsumerService {

    @Value("${spring.kafka.consumer.group-id-at-least-once}")
    private String groupId;

    @Value("${kafka.topic.at-least-once}")
    private String topic;

    @KafkaListener(
            topics = "${kafka.topic.at-least-once}",
            groupId = "${spring.kafka.consumer.group-id-at-least-once}",
            containerFactory = "atLeastOnceKafkaListenerContainerFactory" // don't use the default factory
    )
    public void listenAtLeastOnce(String message, Acknowledgment acknowledgment) {
        try {
            // Simulate random failures (30% chance)
            if (Math.random() < 0.3) {
                throw new RuntimeException("Simulated processing failure");
            }
            handleMessage(message, groupId, topic);
            acknowledgment.acknowledge(); // Manual commit
        } catch (Exception e) {
            System.err.println("Error processing message: " + message);
            e.printStackTrace();
            // Don't acknowledge - will be redelivered
        }
    }
}
