package com.company.backend.kafkademo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceExactlyOnce extends AbstractKafkaConsumerService {

    @Value("${spring.kafka.consumer.group-id-exactly-once}")
    private String groupId;

    @Value("${kafka.topic.exactly-once}")
    private String topic;

    @KafkaListener(
            topics = "${kafka.topic.exactly-once}",
            groupId = "${spring.kafka.consumer.group-id-exactly-once}",
            containerFactory = "ExactlyOnceKafkaListenerContainerFactory"
    )
    public void listenExactlyOnce(String message) {
        handleMessage(message, groupId, topic);
    }
}
