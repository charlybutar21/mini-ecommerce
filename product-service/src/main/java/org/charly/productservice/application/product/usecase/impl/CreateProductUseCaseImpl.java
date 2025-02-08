package org.charly.productservice.application.product.usecase.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charly.productservice.application.product.dto.request.CreateProductRequest;
import org.charly.productservice.application.product.exception.BrandNotFoundException;
import org.charly.productservice.application.product.exception.CategoryNotFoundException;
import org.charly.productservice.application.product.usecase.CreateProductUseCase;
import org.charly.productservice.domain.product.model.Brand;
import org.charly.productservice.domain.product.model.Category;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.BrandRepository;
import org.charly.productservice.domain.product.repository.CategoryRepository;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final ProductRepository productRepository;

    @Override
    public void validate(CreateProductRequest createProductRequest) {
        if (createProductRequest == null) {
            throw new IllegalArgumentException("Product request cannot be null");
        } else if (createProductRequest.getCode() == null || createProductRequest.getCode().isBlank()) {
            throw new IllegalArgumentException("Product code cannot be null or empty");
        }  else if (createProductRequest.getName() == null || createProductRequest.getName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        } else if (createProductRequest.getPrice() == null || createProductRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price cannot be null or less than or equal to 0");
        } else if (createProductRequest.getStock() <= 0) {
            throw new IllegalArgumentException("Product stock cannot be less than or equal to 0");
        } else if (createProductRequest.getCategoryId() == null || createProductRequest.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Product category id cannot be null or less than or equal to 0");
        } else if (createProductRequest.getBrandId() == null || createProductRequest.getBrandId() <= 0) {
            throw new IllegalArgumentException("Product brand id cannot be null or less than or equal to 0");
        }
    }

    @Override
    @Transactional
    public String execute(CreateProductRequest request) {
        validate(request);

        log.info("Creating product with request: {}", request);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new BrandNotFoundException(request.getBrandId()));

        Product product = createProduct(request, category, brand);

        product = productRepository.save(product);

        log.info("Product {} created successfully", product.getCode());

        return product.getCode();
    }

    private Product createProduct(CreateProductRequest request, Category category, Brand brand) {
        return Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .active(true)
                .stock(request.getStock())
                .price(request.getPrice())
                .category(category)
                .brand(brand)
                .build();
    }
}
