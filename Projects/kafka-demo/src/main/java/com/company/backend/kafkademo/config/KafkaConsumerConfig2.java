package com.company.backend.kafkademo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;


@EnableKafka
@Configuration
public class KafkaConsumerConfig2 extends AbstractKafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id-2}")
    public String consumerGroupId;

    private static final int NUM_CONSUMERS = 2;

    @Bean
    public ConsumerFactory<String, String> consumerFactory2() {
        return createConsumerFactory(bootstrapServers, consumerGroupId);
    }

    @Bean(name = "kafkaListenerContainerFactory2") // service 2 should use the bean by name instead of the default factory bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory2() {
        return createListenerContainerFactory(consumerFactory2(), NUM_CONSUMERS);
    }
}

