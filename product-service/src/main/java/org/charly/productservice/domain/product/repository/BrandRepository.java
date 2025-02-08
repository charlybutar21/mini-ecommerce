package org.charly.productservice.domain.product.repository;

import org.charly.productservice.domain.product.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {}
