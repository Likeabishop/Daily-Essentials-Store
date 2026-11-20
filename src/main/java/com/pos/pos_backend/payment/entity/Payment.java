package com.pos.pos_backend.payment.entity;

import java.time.OffsetDateTime;

import com.pos.pos_backend.entity.BaseEntity;
import com.pos.pos_backend.payment.enums.PaymentMethod;
import com.pos.pos_backend.payment.enums.PaymentStatus;
import com.pos.pos_backend.sale.entity.Sale;
import com.pos.pos_backend.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Sale sale;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String proofReference;

    private boolean proofReceived;

    @ManyToOne(fetch = FetchType.LAZY)
    private User confirmedBy;

    private OffsetDateTime confirmedAt;

    private String notes;
}