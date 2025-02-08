package org.charly.productservice.interfaces.product.controller;

import lombok.RequiredArgsConstructor;
import org.charly.productservice.application.product.usecase.DeleteProductUseCase;
import org.charly.productservice.common.dto.response.WebRestApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class DeleteProductController {

    private final DeleteProductUseCase deleteProductUseCase;

    @PatchMapping("/{code}")
    public WebRestApiResponse<String> deleteProduct(@PathVariable String code) {
        String productCode = deleteProductUseCase.execute(code);

        return WebRestApiResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(productCode)
                .build();
    }

}
