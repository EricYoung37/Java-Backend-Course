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
        kafkaTemplate.send(topic, key, message);
    }
}
