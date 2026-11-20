package com.pos.pos_backend.analytics.service;

import java.math.BigDecimal;

public interface RevenueSummaryProjection {

    BigDecimal getRevenue();

    Long getTransactions();

}