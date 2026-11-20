package com.pos.pos_backend.analytics.dto;

import java.math.BigDecimal;

public record AnalyticsSummaryResponse(

        BigDecimal totalRevenue,

        Long totalTransactions,

        BigDecimal averageSaleValue
) {
}
