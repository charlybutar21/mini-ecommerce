package org.charly.productservice.application.product.usecase.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charly.productservice.application.product.dto.request.UpdateProductRequest;
import org.charly.productservice.application.product.exception.BrandNotFoundException;
import org.charly.productservice.application.product.exception.CategoryNotFoundException;
import org.charly.productservice.application.product.exception.ProductNotFoundException;
import org.charly.productservice.application.product.usecase.UpdateProductUseCase;
import org.charly.productservice.domain.product.model.Brand;
import org.charly.productservice.domain.product.model.Category;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.BrandRepository;
import org.charly.productservice.domain.product.repository.CategoryRepository;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final ProductRepository productRepository;

    private final StringRedisTemplate redisTemplate;

    private static final String PRODUCT_CACHE_PREFIX = "product_";

    @Override
    public void validate(UpdateProductRequest updateProductRequest) {
        if (updateProductRequest == null) {
            throw new IllegalArgumentException("Update product request cannot be null");
        }else if (updateProductRequest.getCode() == null || updateProductRequest.getCode().isBlank()) {
            throw new IllegalArgumentException("Product code cannot be null or empty");
        }
    }

    @Override
    @Transactional
    public String execute(UpdateProductRequest request) {
        validate(request);

        log.info("Updating product with request: {}", request);

        Product product = productRepository.findByCode(request.getCode())
                .orElseThrow(() -> new ProductNotFoundException(request.getCode()));

        updateProductDetails(product, request);

        redisTemplate.delete(PRODUCT_CACHE_PREFIX + request.getCode());

        log.info("Product updated successfully");

        return product.getCode();
    }

    private void updateProductDetails(Product product, UpdateProductRequest request) {
        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getStock() >= 0) {
            product.setStock(request.getStock());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new BrandNotFoundException(request.getBrandId()));
            product.setBrand(brand);
        }

        productRepository.save(product);
    }
}
