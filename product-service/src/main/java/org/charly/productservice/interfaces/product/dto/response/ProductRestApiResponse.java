package org.charly.productservice.interfaces.product.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRestApiResponse {
    private String code;
    private String name;
    private String description;
    private int stock;
    private BigDecimal price;
    private CategoryRestApiResponse category;
    private BrandRestApiResponse brand;
}