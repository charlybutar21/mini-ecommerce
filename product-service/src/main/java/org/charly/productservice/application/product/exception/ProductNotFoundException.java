package org.charly.productservice.application.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String code) {
        super("Product with code " + code + " not found");
    }
}
