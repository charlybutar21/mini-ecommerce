package org.charly.productservice.application.product.exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(Long id) {
        super("Brand with ID " + id + " not found");
    }
}
