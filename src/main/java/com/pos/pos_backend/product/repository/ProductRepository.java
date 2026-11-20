package com.pos.pos_backend.product.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pos.pos_backend.product.entity.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndBusinessId(
            Long id,
            Long businessId
    );

    Page<Product> findByBusinessId(
            Long businessId,
            Pageable pageable
    );

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.business.id = :businessId
        AND (
            lower(p.name)
            LIKE lower(concat('%', :search, '%'))
        )
    """)
    Page<Product> search(
            Long businessId,
            String search,
            Pageable pageable
    );
}