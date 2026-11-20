package com.pos.pos_backend.analytics.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailySalesProjection {

    LocalDate getSaleDate();

    BigDecimal getRevenue();

    Long getTransactions();
}