package com.company.backend.kafkademo.service;

public abstract class AbstractKafkaConsumerService {

    protected void handleMessage(String message, String consumerGroupId, String topic) {
        System.out.println("Received message: " + message +
                " from group: " + consumerGroupId +
                " with topic: " + topic);
//        {"messageKey":1234, // idempotency key}
//        try {
//            //process and persist message
//        } catch (Exception e) {
//            //send to dead letter queue
//        }
//        commit to offset here
//        return
    }
}