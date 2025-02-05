package org.charly.productservice.service;

import lombok.RequiredArgsConstructor;
import org.charly.productservice.dto.response.ProductResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, ProductResponse> kafkaTemplate;

    public void sendProductEvent(ProductResponse product) {
        kafkaTemplate.send("product-events", product);
    }
}