package com.pos.pos_backend.sale.repository;

import com.pos.pos_backend.sale.entity.Sale;
import com.pos.pos_backend.sale.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleItemRepository
        extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findBySale(Sale sale);
}