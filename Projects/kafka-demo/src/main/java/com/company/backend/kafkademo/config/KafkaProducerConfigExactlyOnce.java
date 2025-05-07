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
public class KafkaProducerConfigExactlyOnce extends AbstractKafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private ProducerFactory<String, String> exactlyOnceProducerFactory() {
        Map<String, Object> configProps = commonConfig();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(configProps);
        factory.setTransactionIdPrefix("txn-id-exactly-once-"); // Needed for transactions
        return factory;
    }

    @Bean(name = "exactlyOnceKafkaTemplate")
    public KafkaTemplate<String, String> exactlyOnceKafkaTemplate() {
        return new KafkaTemplate<>(exactlyOnceProducerFactory());
    }
}
