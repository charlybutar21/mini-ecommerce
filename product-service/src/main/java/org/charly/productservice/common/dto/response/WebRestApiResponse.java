package org.charly.productservice.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebRestApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private List<ErrorDetail> errors;
    private PagingRestApiResponse paging;
}
