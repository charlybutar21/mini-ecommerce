package org.charly.productservice.service;

import org.charly.productservice.dto.request.CreateProductRequest;
import org.charly.productservice.dto.request.SearchProductRequest;
import org.charly.productservice.dto.request.UpdateProductRequest;
import org.charly.productservice.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
    ProductResponse getProductById(Long id);
    Page<ProductResponse> searchProducts(SearchProductRequest request, Pageable pageable);
}
