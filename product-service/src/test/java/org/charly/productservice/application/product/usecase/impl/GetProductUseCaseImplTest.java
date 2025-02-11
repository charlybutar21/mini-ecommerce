package org.charly.productservice.application.product.usecase.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.charly.productservice.application.product.dto.response.ProductResponse;
import org.charly.productservice.application.product.exception.ProductNotFoundException;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductUseCaseImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    StringRedisTemplate redisTemplate;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @InjectMocks
    GetProductUseCaseImpl getProductUseCase;
    private Product product;
    private ProductResponse productResponse;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setCode("P123");

        productResponse = new ProductResponse();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void validate_ShouldThrowException_WhenCodeIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> getProductUseCase.validate(null));
        assertEquals("Product code cannot be null or empty", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenCodeIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> getProductUseCase.validate(""));
        assertEquals("Product code cannot be null or empty", exception.getMessage());
    }

    @Test
    void execute_ShouldReturnProductFromCache() throws JsonProcessingException {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("product_P123")).thenReturn("{\"code\":\"P123\"}");
        when(objectMapper.readValue("{\"code\":\"P123\"}", ProductResponse.class)).thenReturn(productResponse);

        ProductResponse response = getProductUseCase.execute("P123");

        assertNotNull(response);
        verify(productRepository, never()).findByCode(anyString());
    }

    @Test
    void execute_ShouldReturnProductFromDatabase_WhenNotInCache() throws JsonProcessingException {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("product_P123")).thenReturn(null);
        when(productRepository.findByCode("P123")).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductResponse.class)).thenReturn(productResponse);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"code\":\"P123\"}");

        ProductResponse response = getProductUseCase.execute("P123");

        assertNotNull(response);
        verify(productRepository).findByCode("P123");
        verify(valueOperations).set(eq("product_P123"), anyString(), any());
    }

    @Test
    void execute_ShouldThrowException_WhenProductNotFound() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("product_P123")).thenReturn(null);
        when(productRepository.findByCode("P123")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> getProductUseCase.execute("P123"));
    }
}