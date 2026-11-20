package com.pos.pos_backend.analytics.service;

import java.math.BigDecimal;

public interface BestSellerProjection {

    String getProductName();

    Long getQuantitySold();

    BigDecimal getRevenue();
}