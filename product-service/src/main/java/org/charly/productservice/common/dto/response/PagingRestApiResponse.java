package org.charly.productservice.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PagingRestApiResponse {
    private int pageNumber;
    private int totalPages;
    private long totalItems;
    private int pageSize;
}

