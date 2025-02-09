package org.charly.productservice.interfaces.product.controller;

import lombok.RequiredArgsConstructor;
import org.charly.productservice.application.product.dto.response.ProductResponse;
import org.charly.productservice.application.product.usecase.GetProductUseCase;
import org.charly.productservice.common.web.WebRestApiResponse;
import org.charly.productservice.interfaces.product.dto.response.ProductRestApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class GetProductController {

    private final GetProductUseCase getProductUseCase;

    private final ModelMapper modelMapper;

    @GetMapping("/{code}")
    public WebRestApiResponse<ProductRestApiResponse> getProductById(@PathVariable String code) {
        ProductResponse productResponse = getProductUseCase.execute(code);

        return WebRestApiResponse.<ProductRestApiResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(modelMapper.map(productResponse, ProductRestApiResponse.class))
                .build();
    }

}
