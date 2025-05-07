package com.company.backend.kafkademo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceCustomPartition extends AbstractKafkaConsumerService {

    @Value("${spring.kafka.consumer.group-id-custom-partition}")
    private String groupId;

    @Value("${kafka.topic.custom-partition}")
    private String topic;

    @KafkaListener(topics = "${kafka.topic.custom-partition}", groupId = "${spring.kafka.consumer.group-id-custom-partition}")
    public void listenCustomPartition(String message,
                                      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        System.out.println("Received message: " + message + " from partition: " + partition);
        handleMessage(message + " (partition: " + partition + ")", groupId, topic);
    }
}