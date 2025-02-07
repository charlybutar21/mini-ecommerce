package org.charly.productservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charly.productservice.dto.request.CreateProductRequest;
import org.charly.productservice.dto.request.SearchProductRequest;
import org.charly.productservice.dto.request.UpdateProductRequest;
import org.charly.productservice.dto.response.ProductResponse;
import org.charly.productservice.entity.Brand;
import org.charly.productservice.entity.Category;
import org.charly.productservice.entity.Product;
import org.charly.productservice.exception.ProductNotFoundException;
import org.charly.productservice.mapper.ProductMapper;
import org.charly.productservice.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PRODUCT_CACHE_PREFIX = "product_";

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating product: {}", request);

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .category(new Category(request.categoryId(), null))
                .brand(new Brand(request.brandId(), null))
                .price(request.price())
                .build();

        product = productRepository.save(product);
        product = productRepository.findById(product.getId()).orElseThrow();
        ProductResponse response = productMapper.toResponse(product);

        redisTemplate.opsForValue().set(PRODUCT_CACHE_PREFIX + product.getId(), product, Duration.ofHours(1));

        log.info("Product created successfully: {}", response);

        return response;
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Updating product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(request.name());
        product.setCategory(new Category(request.categoryId(), null));
        product.setBrand(new Brand(request.brandId(), null));
        product.setPrice(request.price());

        product = productRepository.save(product);
        ProductResponse response = productMapper.toResponse(product);

        redisTemplate.opsForValue().set(PRODUCT_CACHE_PREFIX + id, product, Duration.ofHours(1));

        log.info("Product updated successfully: {}", response);

        return response;
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);

        redisTemplate.delete(PRODUCT_CACHE_PREFIX + id);

        log.info("Product deleted successfully: {}", id);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with id: {}", id);

        String cacheKey = PRODUCT_CACHE_PREFIX + id;

        Product cachedProduct = (Product) redisTemplate.opsForValue().get(cacheKey);
        if (cachedProduct != null) {
            log.info("Product found in cache: {}", cachedProduct);
            return productMapper.toResponse(cachedProduct);
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        redisTemplate.opsForValue().set(cacheKey, product, Duration.ofHours(1));

        ProductResponse response = productMapper.toResponse(product);
        log.info("Product fetched from DB: {}", response);
        return response;
    }

    @Override
    public Page<ProductResponse> searchProducts(SearchProductRequest request, Pageable pageable) {
        log.info("Searching products with filters: {}", request);

        Specification<Product> spec = Specification.where(null);

        if (StringUtils.hasText(request.keyword())) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + request.keyword().toLowerCase() + "%"));
        }
        if (request.categoryIds() != null && !request.categoryIds().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("category").get("id").in(request.categoryIds()));
        }
        if (request.brandIds() != null && !request.brandIds().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("brand").get("id").in(request.brandIds()));
        }
        if (request.minPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), request.minPrice()));
        }
        if (request.maxPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), request.maxPrice()));
        }

        Page<Product> products = productRepository.findAll(spec, pageable);
        return products.map(productMapper::toResponse);
    }
}
