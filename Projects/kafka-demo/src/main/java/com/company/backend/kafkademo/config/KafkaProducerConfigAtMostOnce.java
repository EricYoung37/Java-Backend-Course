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
public class KafkaProducerConfigAtMostOnce extends AbstractKafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private ProducerFactory<String, String> atMostOnceProducerFactory() {
        Map<String, Object> configProps = commonConfig();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.ACKS_CONFIG, "0"); // Fire-and-forget
        configProps.put(ProducerConfig.RETRIES_CONFIG, 0);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "atMostOnceKafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(atMostOnceProducerFactory());
    }
}
