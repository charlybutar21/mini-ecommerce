package org.charly.productservice.dto.response;

public record PagingResponse(
        int currentPage,
        int totalPage,
        Long size
) {}
