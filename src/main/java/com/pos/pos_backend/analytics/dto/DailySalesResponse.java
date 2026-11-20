package com.pos.pos_backend.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySalesResponse(

        LocalDate date,

        BigDecimal revenue,

        Long transactions
) {
}