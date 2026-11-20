package com.pos.pos_backend.sale.dto;

import java.math.BigDecimal;

public record SaleItemResponse(

        String sku,

        String product,

        String variant,

        BigDecimal unitPrice,

        Integer quantity,

        BigDecimal lineTotal
) {
}