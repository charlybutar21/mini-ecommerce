package org.charly.productservice.dto.response;

import java.math.BigDecimal;

public record UpdateProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long categoryId,
        Long brandId
) {}

