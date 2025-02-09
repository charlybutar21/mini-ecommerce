package org.charly.productservice.application.product.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateProductRequest {
    private String name;

    private String code;

    private String description;

    private BigDecimal price;

    private int stock;

    private Long categoryId;

    private Long brandId;
}
