package com.pos.pos_backend.sale.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SaleSummaryResponse(

        Long saleId,

        String saleReference,

        String paymentMethod,

        String paymentStatus,

        String saleStatus,

        BigDecimal total,

        OffsetDateTime createdAt
) {
}