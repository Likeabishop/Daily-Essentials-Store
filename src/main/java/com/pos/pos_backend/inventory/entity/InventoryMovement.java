package com.pos.pos_backend.inventory.entity;

import java.time.OffsetDateTime;

import com.pos.pos_backend.inventory.dto.InventoryMovementType;
import com.pos.pos_backend.product.entity.ProductVariant;
import com.pos.pos_backend.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_movement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "variant_id",
            nullable = false
    )
    private ProductVariant variant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "performed_by",
            nullable = false
    )
    private User performedBy;

    @Enumerated(EnumType.STRING)
    private InventoryMovementType movementType;

    private Integer quantityChange;

    private String reason;

    private String referenceNumber;

    private OffsetDateTime createdAt;
}