package org.charly.productservice.interfaces.exceptionhandler.controller;

import org.charly.productservice.application.product.exception.BrandNotFoundException;
import org.charly.productservice.application.product.exception.CategoryNotFoundException;
import org.charly.productservice.application.product.exception.ProductNotFoundException;
import org.charly.productservice.common.dto.response.ErrorDetail;
import org.charly.productservice.common.dto.response.WebRestApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;

public class GlobalExceptionHandler {

    @ExceptionHandler({ProductNotFoundException.class, CategoryNotFoundException.class, BrandNotFoundException.class})
    public WebRestApiResponse<Object> handleResourceNotFound(RuntimeException ex) {
        String resource = "Unknown";
        if (ex instanceof BrandNotFoundException) {
            resource = "Brand";
        } else if (ex instanceof CategoryNotFoundException) {
            resource = "Category";
        } else if (ex instanceof ProductNotFoundException) {
            resource = "Product";
        }

        return WebRestApiResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .errors(Collections.singletonList(new ErrorDetail(resource, "not found")))
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public WebRestApiResponse<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorDetail(err.getField(), err.getDefaultMessage()))
                .toList();

        return WebRestApiResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public WebRestApiResponse<Object> handleGenericException(Exception ex) {
        return WebRestApiResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .errors(Collections.singletonList(new ErrorDetail("unknown", ex.getMessage())))
                .build();
    }
}

