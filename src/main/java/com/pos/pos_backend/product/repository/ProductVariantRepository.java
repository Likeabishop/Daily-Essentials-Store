package com.pos.pos_backend.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.pos_backend.product.entity.ProductVariant;

public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long> {

    Optional<ProductVariant>
    findByIdAndProductBusinessId(
            Long id,
            Long businessId
    );

    Optional<ProductVariant>
    findBySku(String sku);

}