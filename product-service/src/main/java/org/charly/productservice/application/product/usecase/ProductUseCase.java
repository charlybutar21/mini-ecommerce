package org.charly.productservice.application.product.usecase;

public interface ProductUseCase<Request, Response> {
    void validate(Request request);
    Response execute(Request request);
}
