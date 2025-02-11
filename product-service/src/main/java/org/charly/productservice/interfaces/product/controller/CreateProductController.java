package org.charly.productservice.interfaces.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.charly.productservice.application.product.dto.request.CreateProductRequest;
import org.charly.productservice.application.product.usecase.CreateProductUseCase;
import org.charly.productservice.interfaces.product.dto.request.CreateProductRestApiRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CreateProductController {

    private final CreateProductUseCase createProductUseCase;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid CreateProductRestApiRequest restApiRequest) {
        CreateProductRequest request = modelMapper
                .map(restApiRequest, CreateProductRequest.class);

        String productCode = createProductUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(productCode);
    }
}
