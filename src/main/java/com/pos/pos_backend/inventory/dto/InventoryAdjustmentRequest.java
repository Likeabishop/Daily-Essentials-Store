package com.pos.pos_backend.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryAdjustmentRequest(

        @NotNull
        Long variantId,

        @NotNull
        Integer quantityAdjustment,

        @NotBlank
        String reason
) {
}