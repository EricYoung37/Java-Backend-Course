package com.company.backend.kafkademo.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaProducerConfigAtLeastOnce extends AbstractKafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private ProducerFactory<String, String> atLeastOnceProducerFactory() {
        Map<String, Object> configProps = commonConfig();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");   // Wait for all replicas to acknowledge
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);    // Retry up to 3 times
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "atLeastOnceKafkaTemplate")
    public KafkaTemplate<String, String> atLeastOnceKafkaTemplate() {
        return new KafkaTemplate<>(atLeastOnceProducerFactory());
    }
}