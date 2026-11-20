package com.pos.pos_backend.inventory.service;

import java.time.OffsetDateTime;

import com.pos.pos_backend.inventory.dto.InventoryMovementType;
import com.pos.pos_backend.inventory.dto.StockInRequest;
import com.pos.pos_backend.inventory.entity.Inventory;
import com.pos.pos_backend.inventory.entity.InventoryMovement;
import com.pos.pos_backend.inventory.repository.InventoryRepository;
import com.pos.pos_backend.user.entity.User;

import jakarta.transaction.Transactional;

public class InventoryService {
    InventoryRepository inventoryRepository;
    //MovementRepository movementRepository;

    public InventoryService(
        InventoryRepository inventoryRepository//,
        //MovementRepository movementRepository
    ) {
        inventoryRepository = this.inventoryRepository;
        //movementRepository = this.movementRepository;
    }

    @Transactional
    public void stockIn(
        StockInRequest request,
        User currentUser
    ) {

        Inventory inventory =
            inventoryRepository
                .findByVariantId(
                        request.variantId()
                )
                .orElseThrow();

        inventory.setQuantityOnHand(
            inventory.getQuantityOnHand()
            + request.quantity()
        );

        /*movementRepository.save(
            InventoryMovement.builder()
                    .variant(
                            inventory.getVariant()
                    )
                    .performedBy(currentUser)
                    .movementType(
                            InventoryMovementType.STOCK_IN
                    )
                    .quantityChange(
                            request.quantity()
                    )
                    .reason(
                            request.reason()
                    )
                    .createdAt(
                            OffsetDateTime.now()
                    )
                    .build()
        );*/
    }
}
