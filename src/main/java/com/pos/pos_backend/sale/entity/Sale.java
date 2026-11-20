package com.pos.pos_backend.sale.entity;

import java.math.BigDecimal;

import com.pos.pos_backend.business.entity.Business;
import com.pos.pos_backend.entity.BaseEntity;
import com.pos.pos_backend.payment.enums.PaymentMethod;
import com.pos.pos_backend.payment.enums.PaymentStatus;
import com.pos.pos_backend.sale.enums.SaleStatus;
import com.pos.pos_backend.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sale")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String saleReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    private User processedBy;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    private BigDecimal subtotal;

    private BigDecimal total;

    private String idempotencyKey;
}