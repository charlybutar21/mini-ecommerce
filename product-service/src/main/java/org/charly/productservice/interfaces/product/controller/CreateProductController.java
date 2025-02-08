package org.charly.productservice.interfaces.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.charly.productservice.application.product.dto.request.CreateProductRequest;
import org.charly.productservice.application.product.usecase.CreateProductUseCase;
import org.charly.productservice.common.dto.response.WebRestApiResponse;
import org.charly.productservice.interfaces.product.dto.request.CreateProductRestApiRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CreateProductController {

    private final CreateProductUseCase createProductUseCase;

    private final ModelMapper modelMapper;

    @PostMapping
    public WebRestApiResponse<String> createProduct(@RequestBody @Valid CreateProductRestApiRequest restApiRequest) {
        CreateProductRequest request = modelMapper
                .map(restApiRequest, CreateProductRequest.class);

        String productCode = createProductUseCase.execute(request);

        return WebRestApiResponse.<String>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.name())
                .data(productCode)
                .build();
    }
}
