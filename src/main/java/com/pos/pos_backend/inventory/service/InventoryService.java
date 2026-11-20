package com.pos.pos_backend.inventory.service;

import java.time.OffsetDateTime;

import com.pos.pos_backend.common.exception.BusinessException;
import com.pos.pos_backend.inventory.dto.InventoryMovementType;
import com.pos.pos_backend.inventory.dto.StockInRequest;
import com.pos.pos_backend.inventory.entity.Inventory;
import com.pos.pos_backend.inventory.entity.InventoryMovement;
import com.pos.pos_backend.inventory.repository.InventoryMovementRepository;
import com.pos.pos_backend.inventory.repository.InventoryRepository;
import com.pos.pos_backend.product.entity.ProductVariant;
import com.pos.pos_backend.user.entity.User;

import jakarta.transaction.Transactional;

public class InventoryService {
    InventoryRepository inventoryRepository;
    InventoryMovementRepository movementRepository;

    public InventoryService(
        InventoryRepository inventoryRepository,
        InventoryMovementRepository movementRepository
    ) {
        inventoryRepository = this.inventoryRepository;
        movementRepository = this.movementRepository;
    }

    @Transactional
    public void stockIn(
        StockInRequest request,
        User currentUser
    ) {

        Inventory inventory =
            inventoryRepository
                .findByVariant_Id(
                        request.variantId()
                )
                .orElseThrow();


        int available =
        inventory.getQuantityOnHand()
        - inventory.getReservedQuantity();

        if (request.quantity() > available) {

            throw new BusinessException(
                "Insufficient stock"
            );
        }

        movementRepository.save(
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
                    .build()
        );
    }

    private void createMovement(

        ProductVariant variant,

        User user,

        InventoryMovementType type,

        Integer quantity,

        String reason,

        String reference

) {

    movementRepository.save(

            InventoryMovement.builder()
                    .variant(variant)
                    .performedBy(user)
                    .movementType(type)
                    .quantityChange(quantity)
                    .reason(reason)
                    .referenceNumber(reference)
                    .build()
    );
}
}
