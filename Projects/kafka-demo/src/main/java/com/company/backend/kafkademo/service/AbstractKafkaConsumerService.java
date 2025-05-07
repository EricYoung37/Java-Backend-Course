package com.company.backend.kafkademo.service;

public class AbstractKafkaConsumerService {
    protected void handleMessage(String message, String consumerGroupId, String topic) {
        System.out.println("Topic: " + topic + " Group: " + consumerGroupId + " received message: " + message);
    }
}
