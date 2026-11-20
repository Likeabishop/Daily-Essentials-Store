package com.pos.pos_backend.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.pos_backend.inventory.entity.InventoryMovement;

public interface InventoryMovementRepository
        extends JpaRepository<InventoryMovement, Long> {

    Page<InventoryMovement> findByVariantId(
            Long variantId,
            Pageable pageable
    );
}