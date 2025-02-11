package org.charly.productservice.application.product.usecase.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charly.productservice.application.product.dto.request.SearchProductRequest;
import org.charly.productservice.application.product.dto.response.ProductResponse;
import org.charly.productservice.application.product.usecase.SearchProductsUseCase;
import org.charly.productservice.domain.product.model.Product;
import org.charly.productservice.domain.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchProductsUseCaseImpl implements SearchProductsUseCase {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    public void validate(SearchProductRequest searchProductRequest) {
        if (searchProductRequest == null) {
            throw new IllegalArgumentException("Search product request cannot be null");
        } else if (searchProductRequest.getPageNumber() == null || searchProductRequest.getPageNumber() < 0) {
            throw new IllegalArgumentException("Page number cannot be null or negative");
        } else if (searchProductRequest.getPageSize() == null || searchProductRequest.getPageSize() < 0) {
            throw new IllegalArgumentException("Page size cannot be null or negative");
        }
    }

    @Override
    public Page<ProductResponse> execute(SearchProductRequest request) {
        validate(request);

        log.info("Searching products with filters: {}", request);

        Specification<Product> spec = buildSpecification(request);

        Pageable pageable = buildPageable(request);

        Page<Product> products = productRepository.findAll(spec, pageable);

        return products.map(product -> modelMapper.map(product, ProductResponse.class));
    }

    private Specification<Product> buildSpecification(SearchProductRequest request) {
        Specification<Product> spec = (root, query, cb) -> cb.equal(root.get("active"), true);

        if (StringUtils.hasText(request.getKeyword())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + request.getKeyword().toLowerCase() + "%")
            );
        }
        if (!CollectionUtils.isEmpty(request.getCategoryIds())) {
            spec = spec.and((root, query, cb) ->
                    root.get("category").get("id").in(request.getCategoryIds())
            );
        }
        if (!CollectionUtils.isEmpty(request.getBrandIds())) {
            spec = spec.and((root, query, cb) ->
                    root.get("brand").get("id").in(request.getBrandIds())
            );
        }
        if (request.getMinPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), request.getMinPrice())
            );
        }
        if (request.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), request.getMaxPrice())
            );
        }
        return spec;

    }

    private Pageable buildPageable(SearchProductRequest request) {
        if (request.getPageNumber() == null || request.getPageSize() == null) {
            return Pageable.unpaged();
        }

        if (StringUtils.hasText(request.getSortBy()) && StringUtils.hasText(request.getDirection())) {
            Sort sort = Sort.by(request.getDirection().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, request.getSortBy());
            return PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
        }

        return PageRequest.of(request.getPageNumber(), request.getPageSize());
    }
}
