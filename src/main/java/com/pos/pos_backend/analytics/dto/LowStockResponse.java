package com.pos.pos_backend.analytics.dto;

public record LowStockResponse(

        Long variantId,

        String sku,

        String product,

        String variant,

        Integer quantityOnHand,

        Integer reservedQuantity,

        Integer availableQuantity,

        Integer reorderLevel
) {
}