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
public class KafkaProducerConfigCustomPartition extends AbstractKafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic.custom-partition}")
    private String topic;

    private ProducerFactory<String, String> customPartitionProducerFactory() {
        Map<String, Object> configProps = commonConfig();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "customPartitionKafkaTemplate")
    public KafkaTemplate<String, String> customPartitionKafkaTemplate() {
        return new KafkaTemplate<>(customPartitionProducerFactory());
    }
}