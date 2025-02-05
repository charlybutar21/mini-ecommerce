package org.charly.productservice.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record SearchProductRequest(
        String keyword,
        List<Long> categoryIds,
        List<Long> brandIds,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {}
