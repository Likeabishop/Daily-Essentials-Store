package com.pos.pos_backend.analytics.dto;

import java.math.BigDecimal;

public record BestSellerResponse(

        String product,

        Long quantitySold,

        BigDecimal revenue
) {
}