package com.pos.pos_backend.sale.dto;

import java.util.List;

import com.pos.pos_backend.payment.enums.PaymentMethod;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateSaleRequest(

        @NotEmpty
        List<CreateSaleItemRequest> items,

        @NotNull
        PaymentMethod paymentMethod
) {
}