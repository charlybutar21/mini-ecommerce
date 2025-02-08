package org.charly.productservice.application.product.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private String code;

    private String name;

    private String description;

    private int stock;

    private BigDecimal price;

    private CategoryResponse category;

    private BrandResponse brand;
}
