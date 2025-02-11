package org.charly.productservice.application.product.usecase.impl;

import org.charly.productservice.application.product.dto.request.UpdateProductRequest;
import org.charly.productservice.application.product.exception.BrandNotFoundException;
import org.charly.productservice.application.product.exception.CategoryNotFoundException;
import org.charly.productservice.domain.product.model.Brand;
import org.charly.productservice.domain.product.model.Category;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.BrandRepository;
import org.charly.productservice.domain.product.repository.CategoryRepository;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateProductUseCaseImplTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    BrandRepository brandRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    StringRedisTemplate redisTemplate;
    @InjectMocks
    UpdateProductUseCaseImpl updateProductUseCase;
    private UpdateProductRequest request;
    private Product product;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        request = new UpdateProductRequest();
        request.setCode("P123");
        request.setName("Updated Product");
        request.setDescription("Updated Description");
        request.setPrice(BigDecimal.valueOf(99.99));
        request.setStock(10);
        request.setCategoryId(1L);
        request.setBrandId(2L);

        product = new Product();
        product.setCode("P123");
        product.setName("Old Product");
        product.setDescription("Old Description");
        product.setPrice(BigDecimal.valueOf(49.99));
        product.setStock(5);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void validate_ShouldThrowException_WhenRequestIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> updateProductUseCase.validate(null));
        assertEquals("Update product request cannot be null", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenProductCodeIsNull() {
        this.request.setCode(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> updateProductUseCase.validate(this.request));
        assertEquals("Product code cannot be null or empty", exception.getMessage());
    }

    @Test
    void execute_ShouldUpdateProductSuccessfully() {
        when(productRepository.findByCode("P123")).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));
        when(brandRepository.findById(2L)).thenReturn(Optional.of(new Brand()));

        String result = updateProductUseCase.execute(request);

        assertEquals("P123", result);
        verify(productRepository).save(product);
        verify(redisTemplate).delete("product_P123");
    }

    @Test
    void execute_ShouldThrowException_WhenCategoryNotFound() {
        when(productRepository.findByCode("P123")).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> updateProductUseCase.execute(request));
    }

    @Test
    void execute_ShouldThrowException_WhenBrandNotFound() {
        when(productRepository.findByCode("P123")).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));
        when(brandRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(BrandNotFoundException.class, () -> updateProductUseCase.execute(request));
    }
}