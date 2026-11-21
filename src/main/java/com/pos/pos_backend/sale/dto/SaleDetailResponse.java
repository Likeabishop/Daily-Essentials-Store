package com.pos.pos_backend.sale.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record SaleDetailResponse(

        Long saleId,

        String saleReference,

        String paymentMethod,

        String paymentStatus,

        String saleStatus,

        BigDecimal subtotal,

        BigDecimal total,

        List<SaleItemResponse> items,

        OffsetDateTime createdAt
) {
}