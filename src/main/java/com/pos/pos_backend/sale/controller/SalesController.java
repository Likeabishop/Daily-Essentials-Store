package com.pos.pos_backend.sale.controller;

import com.pos.pos_backend.sale.dto.CreateSaleRequest;
import com.pos.pos_backend.sale.dto.SaleResponse;
import com.pos.pos_backend.sale.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SalesController {
 
    private final SaleService saleService;


    @PostMapping
    public SaleResponse createSale(

            @RequestHeader("Idempotency-Key")
            String key,

            @Valid
            @RequestBody
            CreateSaleRequest request
    ) {

        return saleService.createSale(
                request,
                key
        );
    }
}