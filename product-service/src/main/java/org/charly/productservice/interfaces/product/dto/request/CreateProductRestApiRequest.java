package org.charly.productservice.interfaces.product.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRestApiRequest {

        @NotBlank(message = "Product name is required")
        private String name;

        @NotBlank(message = "Product code is required")
        private String code;

        private String description;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
        private BigDecimal price;

        @Min(value = 0, message = "Stock must be at least 1")
        private int stock;

        @NotNull(message = "Category id is required")
        private Long categoryId;

        @NotNull(message = "Brand id is required")
        private Long brandId;
}

