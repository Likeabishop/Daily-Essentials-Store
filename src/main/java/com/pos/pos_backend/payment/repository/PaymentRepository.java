package com.pos.pos_backend.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.pos_backend.payment.entity.Payment;
import com.pos.pos_backend.sale.entity.Sale;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findBySale(Sale sale);

    Optional<Payment> findBySaleId(Long saleId);
}