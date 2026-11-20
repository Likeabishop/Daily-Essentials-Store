package com.pos.pos_backend.sale.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pos.pos_backend.common.exception.ResourceNotFoundException;
import com.pos.pos_backend.sale.dto.SaleDetailResponse;
import com.pos.pos_backend.sale.dto.SaleItemResponse;
import com.pos.pos_backend.sale.dto.SaleSummaryResponse;
import com.pos.pos_backend.sale.entity.Sale;
import com.pos.pos_backend.sale.entity.SaleItem;
import com.pos.pos_backend.sale.repository.SaleItemRepository;
import com.pos.pos_backend.sale.repository.SaleRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class SalesHistoryService {
    SaleRepository saleRepository;
    SaleItemRepository saleItemRepository;

    @Transactional(readOnly = true)
    public Page<SaleSummaryResponse> getSales(
            Long businessId,
            Pageable pageable
    ) {

        return saleRepository
                .findByBusinessId(
                        businessId,
                        pageable
                )
                .map(this::mapSaleSummary);
    }

    @Transactional(readOnly = true)
    public SaleDetailResponse getSale(
            Long saleId,
            Long businessId
    ) {

        Sale sale =
                saleRepository
                        .findByIdAndBusinessId(
                                saleId,
                                businessId
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Sale not found"
                                )
                        );

        List<SaleItemResponse> items =
                saleItemRepository
                        .findBySale(sale)
                        .stream()
                        .map(this::mapSaleItem)
                        .toList();

        return new SaleDetailResponse(
                sale.getId(),
                sale.getSaleReference(),
                sale.getPaymentMethod().name(),
                sale.getPaymentStatus().name(),
                sale.getSaleStatus().name(),
                sale.getSubtotal(),
                sale.getTotal(),
                items,
                sale.getCreatedAt()
        );
    }

    private SaleSummaryResponse mapSaleSummary(
        Sale sale
    ) {

        return new SaleSummaryResponse(
                sale.getId(),
                sale.getSaleReference(),
                sale.getPaymentMethod().name(),
                sale.getPaymentStatus().name(),
                sale.getSaleStatus().name(),
                sale.getTotal(),
                sale.getCreatedAt()
        );
    }

    private SaleItemResponse mapSaleItem(
        SaleItem item
    ) {

        return new SaleItemResponse(
                item.getSkuSnapshot(),
                item.getProductNameSnapshot(),
                item.getVariantNameSnapshot(),
                item.getUnitPriceSnapshot(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }
}
