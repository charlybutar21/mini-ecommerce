package org.charly.productservice.infrastructure.kafka.producer;

public interface KafkaProducerService {
    void sendMessage(String topic, String message);
}
