package com.company.backend.kafkademo.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaBrokerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapServers;

    @Value("${kafka.topic.at-least-once}")
    private String atLeastOnceTopic;

    @Value("${kafka.topic.at-most-once}")
    private String atMostOnceTopic;

    @Value("${kafka.topic.exactly-once}")
    private String exactlyOnceTopic;

    private static final int NUM_PARTITIONS = 3;
    private static final short REPLICATION_FACTOR = 2;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic atLeastOnceTopic() {
        return new NewTopic(atLeastOnceTopic, NUM_PARTITIONS, REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic atMostOnceTopic() {
        return new NewTopic(atMostOnceTopic, NUM_PARTITIONS, REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic exactlyOnceTopic() {
        return new NewTopic(exactlyOnceTopic, NUM_PARTITIONS, REPLICATION_FACTOR);
    }

    @Value("${kafka.topic.custom-partition}")
    private String customPartitionTopic;

    @Bean
    public NewTopic customPartitionTopic() {
        return new NewTopic(customPartitionTopic, NUM_PARTITIONS, REPLICATION_FACTOR);
    }
}
