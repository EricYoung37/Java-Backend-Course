package com.company.backend.kafkademo.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumerService extends AbstractKafkaConsumerService {

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${kafka.topic.name}")
    private String topic;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenGroupFoo(String message) {
        handleMessage(message, consumerGroupId, topic);
    }
}
