package org.charly.productservice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charly.productservice.dto.response.ProductResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.charly.productservice.producer.ProductEventConstant.TOPIC;
import static org.charly.productservice.producer.ProductEventConstant.TOPIC_CREATED_KEY;
import static org.charly.productservice.producer.ProductEventConstant.TOPIC_DELETED_KEY;
import static org.charly.productservice.producer.ProductEventConstant.TOPIC_UPDATED_KEY;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventProducer {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendProductCreated(ProductResponse response) {
        log.info("Publishing product created event: {}", response);
        kafkaTemplate.send(TOPIC, TOPIC_CREATED_KEY, response);
    }

    public void sendProductUpdated(ProductResponse response) {
        log.info("Publishing product updated event: {}", response);
        kafkaTemplate.send(TOPIC, TOPIC_UPDATED_KEY, response);
    }

    public void sendProductDeleted(Long id) {
        log.info("Publishing product deleted event: {}", id);
        kafkaTemplate.send(TOPIC, TOPIC_DELETED_KEY, id);
    }
}
