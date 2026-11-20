package com.pos.pos_backend.product.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(

        @NotBlank
        String name,

        String description,

        @NotNull
        Long categoryId,

        @NotEmpty
        List<CreateVariantRequest> variants
) {
}