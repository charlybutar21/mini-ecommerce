package org.charly.productservice.application.product.usecase.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charly.productservice.application.product.exception.ProductNotFoundException;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.charly.productservice.application.product.usecase.DeleteProductUseCase;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
    public class DeleteProductUseCaseImpl implements DeleteProductUseCase {

    private static final String PRODUCT_CACHE_PREFIX = "product_";

    private final ProductRepository productRepository;

    private final StringRedisTemplate redisTemplate;

    @Override
    public void validate(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Product code cannot be null or empty");
        }
    }

    @Override
    public String execute(String code) {
        validate(code);

        log.info("Deleting product with code: {}", code);

        productRepository.findByCode(code).ifPresentOrElse(product -> {
            product.setActive(false);

            productRepository.save(product);

            redisTemplate.delete(PRODUCT_CACHE_PREFIX + code);

            log.info("Product {} deleted successfully", code);
        }, () -> {
            throw new ProductNotFoundException(code);
        });

        return code;
    }
}
