package com.company.backend.kafkademo.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumerService2 extends AbstractKafkaConsumerService {

    @Value("${spring.kafka.consumer.group-id-2}")
    private String consumerGroupId;

    @Value("${kafka.topic.name}")
    private String topic;

    @KafkaListener(
            topics = "${kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id-2}",
            containerFactory = "kafkaListenerContainerFactory2" // if not specified, will use the default factory bean
    )
    public void listenGroupFoo(String message) {
        handleMessage(message, consumerGroupId, topic);
    }
}
