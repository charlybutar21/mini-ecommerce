package org.charly.productservice.interfaces.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.charly.productservice.application.product.dto.request.UpdateProductRequest;
import org.charly.productservice.application.product.usecase.UpdateProductUseCase;
import org.charly.productservice.common.web.WebRestApiResponse;
import org.charly.productservice.interfaces.product.dto.request.UpdateProductRestApiRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class UpdateProductController {

    private final UpdateProductUseCase updateProductUseCase;

    private final ModelMapper modelMapper;

    @PutMapping("/{code}")
    public WebRestApiResponse<String> updateProduct(@PathVariable String code, @RequestBody @Valid UpdateProductRestApiRequest restApiRequest) {
        UpdateProductRequest request = modelMapper
                .map(restApiRequest, UpdateProductRequest.class);
        request.setCode(code);

        String productCode = updateProductUseCase.execute(request);

        return WebRestApiResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(productCode)
                .build();
    }
}
