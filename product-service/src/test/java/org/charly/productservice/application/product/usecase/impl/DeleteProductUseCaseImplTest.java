package org.charly.productservice.application.product.usecase.impl;

import org.charly.productservice.application.product.exception.ProductNotFoundException;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteProductUseCaseImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    StringRedisTemplate redisTemplate;
    @InjectMocks
    DeleteProductUseCaseImpl deleteProductUseCase;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void shouldThrowExceptionWhenProductCodeIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> deleteProductUseCase.validate(""));
    }

    @Test
    void shouldThrowExceptionWhenProductCodeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> deleteProductUseCase.validate(null));
    }

    @Test
    void shouldPassWhenProductCodeIsValid() {
        deleteProductUseCase.validate("P123");
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        Product product = Product.builder().code("P123").build();
        when(productRepository.findByCode(anyString())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(redisTemplate.delete(anyString())).thenReturn(Boolean.TRUE);

        String result = deleteProductUseCase.execute("P123");
        assertEquals("P123", result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        String productCode = "P123";
        when(productRepository.findByCode(productCode)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> deleteProductUseCase.execute(productCode));

        assertEquals("Product with code " + productCode + " not found", exception.getMessage());

        verify(productRepository, times(1)).findByCode(productCode);
        verifyNoMoreInteractions(productRepository, redisTemplate);

    }


}