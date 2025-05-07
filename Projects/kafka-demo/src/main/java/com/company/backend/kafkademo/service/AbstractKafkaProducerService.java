package com.company.backend.kafkademo.service;

public abstract class AbstractKafkaProducerService {
    public abstract void sendMessage(String key, String message);
}
