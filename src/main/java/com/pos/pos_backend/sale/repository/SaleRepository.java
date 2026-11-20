package com.pos.pos_backend.sale.repository;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pos.pos_backend.analytics.service.RevenueSummaryProjection;
import com.pos.pos_backend.sale.entity.Sale;

public interface SaleRepository
        extends JpaRepository<Sale, Long> {

    Optional<Sale> findByIdAndBusinessId(
            Long id,
            Long businessId
    );

    Optional<Sale> findByIdempotencyKey(
            String idempotencyKey
    );

    Page<Sale> findByBusinessId(
            Long businessId,
            Pageable pageable
    );

    Page<Sale> findByBusinessIdAndCreatedAtBetween(
            Long businessId,
            OffsetDateTime start,
            OffsetDateTime end,
            Pageable pageable
    );

    @Query("""
        SELECT

        COALESCE(SUM(s.total), 0) AS revenue,

        COUNT(s.id) AS transactions

        FROM Sale s

        WHERE s.business.id = :businessId

        AND s.paymentStatus = 'PAID'

    """)
    RevenueSummaryProjection getSummary(
        @Param("businessId")
        Long businessId
    );
}