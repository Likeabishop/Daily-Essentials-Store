package com.pos.pos_backend.repository;

import com.pos.pos_backend.analytics.repository.AnalyticsRepository;
import com.pos.pos_backend.analytics.service.RevenueSummaryProjection;
import com.pos.pos_backend.business.entity.Business;
import com.pos.pos_backend.business.repository.BusinessRepository;
import com.pos.pos_backend.config.AbstractIntegrationTest;
import com.pos.pos_backend.entity.*;
import com.pos.pos_backend.payment.entity.Payment;
import com.pos.pos_backend.payment.enums.PaymentStatus;
import com.pos.pos_backend.payment.repository.PaymentRepository;
import com.pos.pos_backend.sale.entity.Sale;
import com.pos.pos_backend.sale.repository.SaleRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void revenue_should_ignore_pending_payments() {

        // Arrange

        Business business = new Business();
        business.setName("Business A");
        business = businessRepository.save(business);

        Sale paidSale = new Sale();
        paidSale.setBusiness(business);
        paidSale.setTotal(new BigDecimal("100.00"));
        paidSale = saleRepository.save(paidSale);

        Payment paidPayment = new Payment();
        paidPayment.setSale(paidSale);
        paidPayment.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(paidPayment);

        Sale pendingSale = new Sale();
        pendingSale.setBusiness(business);
        pendingSale.setTotal(new BigDecimal("50.00"));
        pendingSale = saleRepository.save(pendingSale);

        Payment pendingPayment = new Payment();
        pendingPayment.setSale(pendingSale);
        pendingPayment.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.save(pendingPayment);

        // Act

        RevenueSummaryProjection summary =
                analyticsRepository.getSummary(
                        business.getId()
                );

        // Assert

        assertThat(summary.getRevenue())
                .isEqualByComparingTo("100.00");

        assertThat(summary.getTransactions())
                .isEqualTo(1L);
    }

    @Test
    void revenue_should_be_zero_when_no_paid_sales_exist() {

        Business business = new Business();
        business.setName("Business B");
        business = businessRepository.save(business);

        RevenueSummaryProjection summary =
                analyticsRepository.getSummary(
                        business.getId()
                );

        assertThat(summary.getRevenue())
                .isEqualByComparingTo("0.00");

        assertThat(summary.getTransactions())
                .isEqualTo(0L);
    }
}