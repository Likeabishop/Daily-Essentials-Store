package com.pos.pos_backend.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.pos.pos_backend.inventory.entity.Inventory;

import jakarta.persistence.LockModeType;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inventory> findByVariantId(
            Long variantId
    );
}