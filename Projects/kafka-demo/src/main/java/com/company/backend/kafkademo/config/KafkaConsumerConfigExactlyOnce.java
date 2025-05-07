package com.company.backend.kafkademo.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;


@EnableKafka
@Configuration
public class KafkaConsumerConfigExactlyOnce extends AbstractKafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id-exactly-once}")
    private String groupId;

    private static final int NUM_CONSUMERS = 3;

    private ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = commonConfig();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed"); // Critical for exactly-once
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean(name = "ExactlyOnceKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        return createListenerContainerFactory(consumerFactory(), NUM_CONSUMERS);
    }
}