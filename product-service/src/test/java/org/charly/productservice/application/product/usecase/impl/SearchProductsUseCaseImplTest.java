package org.charly.productservice.application.product.usecase.impl;

import org.charly.productservice.application.product.dto.request.SearchProductRequest;
import org.charly.productservice.application.product.dto.response.ProductResponse;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchProductsUseCaseImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    SearchProductsUseCaseImpl searchProductsUseCase;
    private SearchProductRequest searchProductRequest;
    private Product product;
    private ProductResponse productResponse;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        searchProductRequest = new SearchProductRequest();
        searchProductRequest.setPageNumber(0);
        searchProductRequest.setPageSize(10);

        product = new Product();
        productResponse = new ProductResponse();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void validate_ShouldThrowException_WhenRequestIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> searchProductsUseCase.validate(null));
        assertEquals("Search product request cannot be null", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenPageNumberIsNegative() {
        searchProductRequest.setPageNumber(-1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> searchProductsUseCase.validate(searchProductRequest));
        assertEquals("Page number cannot be null or negative", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenPageSizeIsNegative() {
        searchProductRequest.setPageSize(-1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> searchProductsUseCase.validate(searchProductRequest));
        assertEquals("Page size cannot be null or negative", exception.getMessage());
    }

    @Test
    void execute_ShouldReturnPagedProducts() {
        Page<Product> pagedProducts = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedProducts);
        when(modelMapper.map(any(Product.class), eq(ProductResponse.class))).thenReturn(productResponse);

        Page<ProductResponse> result = searchProductsUseCase.execute(searchProductRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void execute_ShouldReturnEmptyPage_WhenNoProductsFound() {
        Page<Product> pagedProducts = new PageImpl<>(Collections.emptyList());
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedProducts);

        Page<ProductResponse> result = searchProductsUseCase.execute(searchProductRequest);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
