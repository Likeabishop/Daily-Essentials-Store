package com.pos.pos_backend.analytics.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.transaction.annotation.Transactional;

import com.pos.pos_backend.analytics.dto.AnalyticsSummaryResponse;
import com.pos.pos_backend.sale.repository.SaleRepository;


import lombok.NoArgsConstructor;

@NoArgsConstructor 
public class AnalyticsService {

    SaleRepository saleRepository;

    @Transactional(readOnly = true)
    public AnalyticsSummaryResponse getSummary(
            Long businessId
    ) {

        RevenueSummaryProjection summary =
                saleRepository.getSummary(
                        businessId
                );

        BigDecimal revenue =
                summary.getRevenue();

        Long transactions =
                summary.getTransactions();

        BigDecimal average =
                transactions == 0
                        ? BigDecimal.ZERO
                        : revenue.divide(
                                BigDecimal.valueOf(
                                        transactions
                                ),
                                2,
                                RoundingMode.HALF_UP
                        );

        return new AnalyticsSummaryResponse(
                revenue,
                transactions,
                average
        );
    }
}
