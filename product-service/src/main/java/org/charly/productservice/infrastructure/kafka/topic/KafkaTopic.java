package org.charly.productservice.infrastructure.kafka.topic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KafkaTopic {

    PRODUCT_TOPIC("product-topic");

    private final String topicName;
}
