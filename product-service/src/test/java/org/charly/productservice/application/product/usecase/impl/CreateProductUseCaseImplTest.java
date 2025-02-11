package org.charly.productservice.application.product.usecase.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.charly.productservice.application.product.dto.request.CreateProductRequest;
import org.charly.productservice.domain.product.model.Brand;
import org.charly.productservice.domain.product.model.Category;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.BrandRepository;
import org.charly.productservice.domain.product.repository.CategoryRepository;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.charly.productservice.infrastructure.kafka.producer.KafkaProducerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.charly.productservice.infrastructure.kafka.topic.KafkaTopic.PRODUCT_TOPIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateProductUseCaseImplTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    BrandRepository brandRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    KafkaProducerService kafkaProducerService;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    CreateProductUseCaseImpl createProductUseCase;
    private CreateProductRequest request;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable  = MockitoAnnotations.openMocks(this);
        request = new CreateProductRequest();
        request.setCode("P123");
        request.setName("Product Name");
        request.setDescription("Product Description");
        request.setPrice(BigDecimal.valueOf(1000000));
        request.setStock(10);
        request.setCategoryId(1L);
        request.setBrandId(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(null));
    }

    @Test
    void shouldThrowExceptionWhenCodeIsBlank() {
        request.setCode("");
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenCodeIsNull() {
        request.setCode(null);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        request.setName("");
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        request.setName(null);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZeroOrNegative() {
        request.setPrice(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        request.setPrice(null);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenStockIsZeroOrNegative() {
        request.setStock(-1);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenCategoryIdIsZeroOrNegative() {
        request.setCategoryId(0L);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenCategoryIdIsNull() {
        request.setCategoryId(null);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenBrandIdIsZeroOrNegative() {
        request.setBrandId(0L);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldThrowExceptionWhenBrandIdIsNull() {
        request.setBrandId(null);
        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.validate(request));
    }

    @Test
    void shouldPassWhenAllFieldsAreValid() {
        assertDoesNotThrow(() -> createProductUseCase.validate(request));
    }

    @Test
    void shouldCreateProductSuccessfully() throws JsonProcessingException {
        Category category = new Category();
        Brand brand = new Brand();
        Product product = Product.builder().code("P123").build();

        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        when(brandRepository.findById(request.getBrandId())).thenReturn(Optional.of(brand));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(objectMapper.writeValueAsString(product)).thenReturn("product-json");

        String productCode = createProductUseCase.execute(request);

        assertEquals("P123", productCode);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(kafkaProducerService, times(1)).sendMessage(PRODUCT_TOPIC.getTopicName(), "product-json");
    }
}


