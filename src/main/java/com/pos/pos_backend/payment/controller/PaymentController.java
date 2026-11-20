package com.pos.pos_backend.payment.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.pos.pos_backend.sale.service.SaleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentController {
    private final SaleService saleService;
    
    @PostMapping("/{saleId}/payments/confirm")
    @PreAuthorize("hasRole('OWNER')")
    public void confirmPayment(
            @PathVariable Long saleId
    ) {

        saleService.confirmPayment(saleId);
    }
        
}
