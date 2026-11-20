package com.pos.pos_backend.sale.dto;

import java.math.BigDecimal;

public record SaleResponse(

        Long saleId,

        String saleReference,

        String paymentMethod,

        String paymentStatus,

        String saleStatus,

        BigDecimal total
) {
}