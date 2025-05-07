package com.company.backend.kafkademo.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;


@EnableKafka
@Configuration
public class KafkaConsumerConfigAtLeastOnce extends AbstractKafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id-at-least-once}")
    private String groupId;

    private static final int NUM_CONSUMERS = 3;

    private ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = commonConfig();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Disable auto-commit
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean(name = "atLeastOnceKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                createListenerContainerFactory(consumerFactory(), NUM_CONSUMERS);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL); // Manual commits
        return factory;
    }
}