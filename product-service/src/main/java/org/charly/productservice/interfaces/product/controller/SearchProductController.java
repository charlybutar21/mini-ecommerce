package org.charly.productservice.interfaces.product.controller;

import lombok.RequiredArgsConstructor;
import org.charly.productservice.application.product.dto.request.SearchProductRequest;
import org.charly.productservice.application.product.usecase.SearchProductsUseCase;
import org.charly.productservice.interfaces.product.dto.request.SearchProductRestApiRequest;
import org.charly.productservice.interfaces.product.dto.response.ProductRestApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class SearchProductController {

    private final SearchProductsUseCase searchProductsUseCase;

    private final ModelMapper modelMapper;

    @GetMapping("/search")
    public ResponseEntity<List<ProductRestApiResponse>> searchProducts(SearchProductRestApiRequest apiRequest) {
        SearchProductRequest request = modelMapper.map(apiRequest, SearchProductRequest.class);

        List<ProductRestApiResponse> responseList = searchProductsUseCase.execute(request)
                .map(product -> modelMapper.map(product, ProductRestApiResponse.class))
                .toList();

        return ResponseEntity.ok(responseList);
    }
}
