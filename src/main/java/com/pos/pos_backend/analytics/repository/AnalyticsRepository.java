package com.pos.pos_backend.analytics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.pos.pos_backend.analytics.service.BestSellerProjection;
import com.pos.pos_backend.analytics.service.DailySalesProjection;
import com.pos.pos_backend.analytics.service.RevenueSummaryProjection;
import com.pos.pos_backend.inventory.entity.Inventory;

public interface AnalyticsRepository {
    @Query("""
        SELECT

        COALESCE(SUM(s.total),0) AS revenue,

        COUNT(s.id) AS transactions

        FROM Sale s

        WHERE s.business.id = :businessId

        AND s.paymentStatus =
        com.pos.pos_backend.payment.enums.PaymentStatus.PAID
        """
    )
    RevenueSummaryProjection getSummary(
            Long businessId
    );

    @Query("""
    SELECT

    si.productNameSnapshot
    AS productName,

    SUM(si.quantity)
    AS quantitySold,

    SUM(si.lineTotal)
    AS revenue

    FROM SaleItem si

    JOIN si.sale s

    WHERE
    s.business.id = :businessId

    AND
    s.paymentStatus =
    com.pos.pos_backend.payment.enums.PaymentStatus.PAID

    GROUP BY
    si.productNameSnapshot

    ORDER BY
    SUM(si.quantity) DESC
    """)
    List<BestSellerProjection>
    findBestSellingProducts(
            Long businessId
    );

    @Query("""
        SELECT

        DATE(s.createdAt)
        AS saleDate,

        SUM(s.total)
        AS revenue,

        COUNT(s.id)
        AS transactions

        FROM Sale s

        WHERE s.business.id = :businessId

        AND s.paymentStatus =
        com.pos.pos_backend.payment.enums.PaymentStatus.PAID

        GROUP BY DATE(s.createdAt)

        ORDER BY DATE(s.createdAt)
    """)
    List<DailySalesProjection>
    dailySales(Long businessId);

    @Query("""
        SELECT i

        FROM Inventory i

        WHERE

        i.variant.product.business.id =
        :businessId

        AND

        (i.quantityOnHand - i.reservedQuantity)

        <= i.reorderLevel
    """)
    List<Inventory> findLowStock(
            Long businessId
    );
}
