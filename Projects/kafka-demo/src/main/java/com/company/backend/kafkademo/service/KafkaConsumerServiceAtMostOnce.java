package com.company.backend.kafkademo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceAtMostOnce extends AbstractKafkaConsumerService {

    @Value("${spring.kafka.consumer.group-id-at-most-once}")
    private String groupId;

    @Value("${kafka.topic.at-most-once}")
    private String topic;

    @KafkaListener(
            topics = "${kafka.topic.at-most-once}",
            groupId = "${spring.kafka.consumer.group-id-at-most-once}",
            containerFactory = "atMostOnceKafkaListenerContainerFactory"
    )
    public void listenAtMostOnce(String message) {
        handleMessage(message, groupId, topic);
    }
}
