package org.charly.productservice.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private int statusCode;
    private String message;
    private List<ErrorDetail> errors;
}
