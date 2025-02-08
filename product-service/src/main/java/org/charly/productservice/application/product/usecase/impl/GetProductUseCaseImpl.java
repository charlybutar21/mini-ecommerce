package org.charly.productservice.application.product.usecase.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charly.productservice.application.product.dto.response.ProductResponse;
import org.charly.productservice.application.product.exception.ProductNotFoundException;
import org.charly.productservice.application.product.usecase.GetProductUseCase;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetProductUseCaseImpl implements GetProductUseCase {

    private static final String PRODUCT_CACHE_PREFIX = "product_";

    private static final Duration CACHE_DURATION = Duration.ofHours(1);

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public void validate(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Product code cannot be null or empty");
        }
    }

    @Override
    public ProductResponse execute(String code)  {
        validate(code);

        log.info("Get product with code: {}", code);

        String cacheKey = PRODUCT_CACHE_PREFIX + code;

        try {
            String cachedJson = redisTemplate.opsForValue().get(cacheKey);
            if (cachedJson != null) {
                return objectMapper.readValue(cachedJson, ProductResponse.class);
            }
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse cached product for code {}: {}", code, e.getMessage());
            redisTemplate.delete(cacheKey);
        }

        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException(code));

        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        try {
            String cachedJson = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(cacheKey, cachedJson, CACHE_DURATION);
        } catch (JsonProcessingException e) {
            log.error("Error occurred while serializing response to JSON for cache key: {}. Error: {}", cacheKey, e.getMessage(), e);
        }

        return response;
    }
}
