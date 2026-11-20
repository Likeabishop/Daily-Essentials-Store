package com.pos.pos_backend.sale.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSaleItemRequest(

        @NotNull
        Long variantId,

        @Positive
        Integer quantity
) {
}