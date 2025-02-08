package org.charly.productservice.application.product.usecase;

import org.charly.productservice.application.product.dto.request.SearchProductRequest;
import org.charly.productservice.application.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface SearchProductsUseCase extends ProductUseCase<SearchProductRequest, Page<ProductResponse>> {}
