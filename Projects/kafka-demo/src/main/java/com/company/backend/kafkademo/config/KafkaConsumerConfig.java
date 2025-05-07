package com.company.backend.kafkademo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;


@EnableKafka
@Configuration
public class KafkaConsumerConfig extends AbstractKafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    public String consumerGroupId;

    private static final int NUM_CONSUMERS = 4;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return createConsumerFactory(bootstrapServers, consumerGroupId);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        return createListenerContainerFactory(consumerFactory(), NUM_CONSUMERS);
    }
}
