package org.charly.productservice.common.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetail {
    private String field;
    private String message;
}
