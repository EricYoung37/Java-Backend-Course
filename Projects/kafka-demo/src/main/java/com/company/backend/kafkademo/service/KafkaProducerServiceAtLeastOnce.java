package com.company.backend.kafkademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceAtLeastOnce extends AbstractKafkaProducerService {

    @Value("${kafka.topic.at-least-once}")
    private String topic;

    @Autowired
    @Qualifier("atLeastOnceKafkaTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }
}
