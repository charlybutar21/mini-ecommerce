package org.charly.productservice.interfaces.product.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SearchProductRestApiRequest {
    private String keyword;
    private List<Long> categoryIds;
    private List<Long> brandIds;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    @Builder.Default
    private Integer pageNumber = 0;

    @Builder.Default
    private Integer pageSize = 10;

    private String sortBy;
    private String direction;
}

