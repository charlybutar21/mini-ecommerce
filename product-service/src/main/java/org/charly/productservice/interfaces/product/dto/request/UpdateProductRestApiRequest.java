package org.charly.productservice.interfaces.product.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRestApiRequest {

        private String name;

        private String description;

        private BigDecimal price;

        private Integer stock;

        private Long categoryId;

        private Long brandId;
}

