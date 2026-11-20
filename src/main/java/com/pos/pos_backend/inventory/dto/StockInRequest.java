package com.pos.pos_backend.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockInRequest(

        @NotNull
        Long variantId,

        @Positive
        Integer quantity,

        @NotBlank
        String reason
) {
}