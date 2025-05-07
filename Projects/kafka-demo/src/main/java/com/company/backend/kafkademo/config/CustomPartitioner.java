package com.company.backend.kafkademo.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        // Custom partitioning logic:
        // - If key is numeric, use modulo partitioning
        // - Otherwise, use hash partitioning
        if (key instanceof String) {
            try {
                long numericKey = Long.parseLong((String) key);
                return (int) (numericKey % numPartitions);
            } catch (NumberFormatException e) {
                // Not a numeric key, fall back to hash partitioning
                return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
            }
        }
        return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
    }

    @Override
    public void close() {
        // Nothing to close
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // No special configuration needed
    }
}