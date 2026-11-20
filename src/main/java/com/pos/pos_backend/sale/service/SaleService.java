package com.pos.pos_backend.sale.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pos.pos_backend.common.exception.BusinessException;
import com.pos.pos_backend.common.exception.ResourceNotFoundException;
import com.pos.pos_backend.inventory.dto.InventoryMovementType;
import com.pos.pos_backend.inventory.entity.Inventory;
import com.pos.pos_backend.inventory.entity.InventoryMovement;
import com.pos.pos_backend.inventory.repository.InventoryMovementRepository;
import com.pos.pos_backend.inventory.repository.InventoryRepository;
import com.pos.pos_backend.payment.entity.Payment;
import com.pos.pos_backend.payment.enums.PaymentMethod;
import com.pos.pos_backend.payment.enums.PaymentStatus;
import com.pos.pos_backend.payment.repository.PaymentRepository;
import com.pos.pos_backend.product.entity.ProductVariant;
import com.pos.pos_backend.product.repository.ProductVariantRepository;
import com.pos.pos_backend.sale.dto.CreateSaleItemRequest;
import com.pos.pos_backend.sale.dto.CreateSaleRequest;
import com.pos.pos_backend.sale.dto.SaleResponse;
import com.pos.pos_backend.sale.entity.Sale;
import com.pos.pos_backend.sale.entity.SaleItem;
import com.pos.pos_backend.sale.enums.SaleStatus;
import com.pos.pos_backend.sale.repository.SaleItemRepository;
import com.pos.pos_backend.sale.repository.SaleRepository;
import com.pos.pos_backend.security.UserPrincipal;
import com.pos.pos_backend.user.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final PaymentRepository paymentRepository;

    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository movementRepository;

    private final ProductVariantRepository variantRepository;

    public SaleResponse createSale(
            CreateSaleRequest request,
            String idempotencyKey
    ) {

        Optional<Sale> existing =
                saleRepository.findByIdempotencyKey(
                        idempotencyKey
                );

        if (existing.isPresent()) {
            return mapToResponse(existing.get());
        }

        User currentUser = getCurrentUser();

        Sale sale = Sale.builder()
                .saleReference(generateSaleReference())
                .business(currentUser.getBusiness())
                .processedBy(currentUser)
                .paymentMethod(request.paymentMethod())
                .idempotencyKey(idempotencyKey)
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;

        sale = saleRepository.save(sale);

        List<SaleItem> saleItems = new ArrayList<>();

        for (CreateSaleItemRequest itemRequest
                : request.items()) {

            ProductVariant variant =
                    variantRepository
                            .findByIdAndProductBusinessId(
                                    itemRequest.variantId(),
                                    currentUser
                                            .getBusiness()
                                            .getId()
                            )
                            .orElseThrow(
                                    () -> new ResourceNotFoundException(
                                            "Variant not found"
                                    )
                            );

            Inventory inventory =
                    inventoryRepository
                            .findByVariant_Id(
                                    variant.getId()
                            )
                            .orElseThrow(
                                    () -> new ResourceNotFoundException(
                                            "Inventory not found"
                                    )
                            );

            validateAvailableStock(
                    inventory,
                    itemRequest.quantity(),
                    variant.getSku()
            );

            BigDecimal lineTotal =
                    variant.getSellingPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.quantity()
                                    )
                            );

            subtotal = subtotal.add(lineTotal);

            SaleItem item =
                    SaleItem.builder()
                            .sale(sale)
                            .variant(variant)
                            .skuSnapshot(
                                    variant.getSku()
                            )
                            .productNameSnapshot(
                                    variant.getProduct()
                                            .getName()
                            )
                            .variantNameSnapshot(
                                    variant.getVariantName()
                            )
                            .unitPriceSnapshot(
                                    variant.getSellingPrice()
                            )
                            .quantity(
                                    itemRequest.quantity()
                            )
                            .lineTotal(lineTotal)
                            .build();

            saleItems.add(item);

            if (request.paymentMethod()
                    == PaymentMethod.CASH) {

                inventory.setQuantityOnHand(
                        inventory.getQuantityOnHand()
                                - itemRequest.quantity()
                );

                createMovement(
                        variant,
                        currentUser,
                        InventoryMovementType.SALE,
                        itemRequest.quantity(),
                        "Cash sale",
                        sale.getSaleReference()
                );

            } else {

                inventory.setReservedQuantity(
                        inventory.getReservedQuantity()
                                + itemRequest.quantity()
                );

                createMovement(
                        variant,
                        currentUser,
                        InventoryMovementType.RESERVATION,
                        itemRequest.quantity(),
                        "EFT reservation",
                        sale.getSaleReference()
                );
            }
        }

        saleItemRepository.saveAll(saleItems);

        sale.setSubtotal(subtotal);
        sale.setTotal(subtotal);

        if (request.paymentMethod()
                == PaymentMethod.CASH) {

            sale.setPaymentStatus(
                    PaymentStatus.PAID
            );

            sale.setSaleStatus(
                    SaleStatus.COMPLETED
            );

        } else {

            sale.setPaymentStatus(
                    PaymentStatus.PENDING
            );

            sale.setSaleStatus(
                    SaleStatus.PENDING_PAYMENT
            );
        }

        Payment payment =
                Payment.builder()
                        .sale(sale)
                        .paymentMethod(
                                request.paymentMethod()
                        )
                        .paymentStatus(
                                sale.getPaymentStatus()
                        )
                        .proofReceived(false)
                        .build();

        paymentRepository.save(payment);

        return mapToResponse(saleRepository.save(sale));
    }

    public void confirmPayment(Long saleId) {

        User currentUser = getCurrentUser();

        Sale sale =
                saleRepository.findById(saleId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Sale not found"
                                )
                        );

        if (sale.getPaymentStatus()
                != PaymentStatus.PENDING) {

            throw new BusinessException(
                    "Sale already processed"
            );
        }

        List<SaleItem> items =
                saleItemRepository.findBySale(sale);

        for (SaleItem item : items) {

            Inventory inventory =
                    inventoryRepository
                            .findByVariant_Id(
                                    item.getVariant().getId()
                            )
                            .orElseThrow();

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity()
                            - item.getQuantity()
            );

            inventory.setQuantityOnHand(
                    inventory.getQuantityOnHand()
                            - item.getQuantity()
            );

            createMovement(
                    item.getVariant(),
                    currentUser,
                    InventoryMovementType.SALE,
                    item.getQuantity(),
                    "EFT confirmed",
                    sale.getSaleReference()
            );
        }

        Payment payment =
                paymentRepository
                        .findBySaleId(sale.getId())
                        .orElseThrow();

        payment.setPaymentStatus(
                PaymentStatus.PAID
        );

        payment.setConfirmedBy(currentUser);

        payment.setConfirmedAt(
                OffsetDateTime.now()
        );

        sale.setPaymentStatus(
                PaymentStatus.PAID
        );

        sale.setSaleStatus(
                SaleStatus.COMPLETED
        );
    }

    public void cancelSale(
            Long saleId,
            User currentUser
    ) {

        Sale sale =
                findSaleForBusiness(
                        saleId,
                        currentUser.getBusiness().getId()
                );

        if (sale.getSaleStatus()
                != SaleStatus.PENDING_PAYMENT) {

            throw new BusinessException(
                    "Only pending EFT sales can be cancelled"
            );
        }

        List<SaleItem> items =
                saleItemRepository.findBySale(sale);

        for (SaleItem item : items) {

            Inventory inventory =
                    inventoryRepository
                            .findByVariant_Id(
                                    item.getVariant().getId()
                            )
                            .orElseThrow();

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity()
                            - item.getQuantity()
            );

            createMovement(
                    item.getVariant(),
                    currentUser,
                    InventoryMovementType.RESERVATION_RELEASE,
                    item.getQuantity(),
                    "Sale cancellation",
                    sale.getSaleReference()
            );
        }

        Payment payment =
                paymentRepository
                        .findBySaleId(sale.getId())
                        .orElseThrow();

        payment.setPaymentStatus(
                PaymentStatus.CANCELLED
        );

        sale.setPaymentStatus(
                PaymentStatus.CANCELLED
        );

        sale.setSaleStatus(
                SaleStatus.CANCELLED
        );
    }

    private Sale findSaleForBusiness(
            Long saleId,
            Long businessId
    ) {

        return saleRepository
                .findByIdAndBusinessId(
                        saleId,
                        businessId
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Sale not found"
                        )
                );
    }

    private void validateAvailableStock(
            Inventory inventory,
            int requestedQuantity,
            String sku
    ) {

        int available =
                inventory.getQuantityOnHand()
                        - inventory.getReservedQuantity();

        if (available < requestedQuantity) {

            throw new BusinessException(
                    "Insufficient stock for SKU: "
                            + sku
            );
        }
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

    private String generateSaleReference() {

        return "SALE-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private SaleResponse mapToResponse(
            Sale sale
    ) {

        return new SaleResponse(
                sale.getId(),
                sale.getSaleReference(),
                sale.getPaymentMethod().name(),
                sale.getPaymentStatus().name(),
                sale.getSaleStatus().name(),
                sale.getTotal()
        );
    }

    private User getCurrentUser() {

        UserPrincipal principal =
                (UserPrincipal)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        return principal.getUser();
    }
}