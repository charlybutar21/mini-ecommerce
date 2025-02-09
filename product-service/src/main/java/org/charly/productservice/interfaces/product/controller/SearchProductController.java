package org.charly.productservice.interfaces.product.controller;

import lombok.RequiredArgsConstructor;
import org.charly.productservice.application.product.dto.request.SearchProductRequest;
import org.charly.productservice.application.product.dto.response.ProductResponse;
import org.charly.productservice.application.product.usecase.SearchProductsUseCase;
import org.charly.productservice.common.web.PagingRestApiResponse;
import org.charly.productservice.common.web.WebRestApiResponse;
import org.charly.productservice.interfaces.product.dto.request.SearchProductRestApiRequest;
import org.charly.productservice.interfaces.product.dto.response.ProductRestApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public WebRestApiResponse<List<ProductRestApiResponse>> searchProducts(SearchProductRestApiRequest apiRequest) {
        SearchProductRequest request = modelMapper.map(apiRequest, SearchProductRequest.class);

        Page<ProductResponse> productResponses = searchProductsUseCase.execute(request);

        return WebRestApiResponse.<List<ProductRestApiResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(productResponses
                        .map(productResponse -> modelMapper.map(productResponse, ProductRestApiResponse.class))
                        .toList())
                .paging(PagingRestApiResponse.builder()
                        .pageNumber(productResponses.getNumber())
                        .pageSize(productResponses.getSize())
                        .totalPages(productResponses.getTotalPages())
                        .totalItems(productResponses.getTotalElements())
                        .build())
                .build();
    }
}
