package org.charly.productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.charly.productservice.dto.request.CreateProductRequest;
import org.charly.productservice.dto.request.SearchProductRequest;
import org.charly.productservice.dto.request.UpdateProductRequest;
import org.charly.productservice.dto.response.PagingResponse;
import org.charly.productservice.dto.response.ProductResponse;
import org.charly.productservice.dto.response.WebResponse;
import org.charly.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) {
        ProductResponse productResponse = productService.createProduct(request);
        return WebResponse.<ProductResponse>builder()
                .data(productResponse)
                .build();
    }

    @PutMapping("/{id}")
    public WebResponse<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request) {
        ProductResponse productResponse = productService.updateProduct(id, request);
        return WebResponse.<ProductResponse>builder()
                .data(productResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public WebResponse<Long> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return WebResponse.<Long>builder().data(id).build();
    }

    @GetMapping("/{id}")
    public WebResponse<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        return WebResponse.<ProductResponse>builder()
                .data(productResponse)
                .build();
    }

    @GetMapping("/search")
    public WebResponse<Page<ProductResponse>> searchProducts(SearchProductRequest request, Pageable pageable) {
        Page<ProductResponse> productResponses = productService.searchProducts(request, pageable);
        PagingResponse pagingResponse = new PagingResponse(pageable.getPageNumber(), pageable.getPageSize(), productResponses.getTotalElements());
        return WebResponse.<Page<ProductResponse>>builder()
                .data(productResponses)
                .paging(pagingResponse)
                .build();
    }
}
