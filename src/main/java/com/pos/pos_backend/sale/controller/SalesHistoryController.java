package com.pos.pos_backend.sale.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.pos_backend.sale.dto.SaleDetailResponse;
import com.pos.pos_backend.sale.dto.SaleSummaryResponse;
import com.pos.pos_backend.sale.service.SalesHistoryService;
import com.pos.pos_backend.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SalesHistoryController {

    private final SalesHistoryService service;

    @GetMapping
    public Page<SaleSummaryResponse> getSales(
            Pageable pageable,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return service.getSales(
                principal.getBusinessId(),
                pageable
        );
    }

    @GetMapping("/{saleId}")
    public SaleDetailResponse getSale(
            @PathVariable Long saleId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return service.getSale(
                saleId,
                principal.getBusinessId()
        );
    }
}
