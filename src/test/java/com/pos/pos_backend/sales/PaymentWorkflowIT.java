package com.pos.pos_backend.sales;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pos.pos_backend.config.AbstractIntegrationTest;
import com.pos.pos_backend.inventory.entity.Inventory;
import com.pos.pos_backend.inventory.repository.InventoryRepository;
import com.pos.pos_backend.payment.entity.Payment;
import com.pos.pos_backend.payment.enums.PaymentStatus;
import com.pos.pos_backend.payment.repository.PaymentRepository;
import com.pos.pos_backend.sale.entity.Sale;
import com.pos.pos_backend.sale.repository.SaleRepository;
import com.pos.pos_backend.sale.service.SaleService;

import lombok.NoArgsConstructor;

@SpringBootTest
class PaymentWorkflowIT extends AbstractIntegrationTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Test
    void eft_can_be_confirmed() {

        saleService.confirmPayment(
            saleId
        );

        Payment payment =
        paymentRepository
                .findBySaleId(saleId)
                .orElseThrow();

        assertEquals(
                PaymentStatus.PAID,
                payment.getPaymentStatus()
        );
    }

    @Test
    void eft_can_be_cancelled() {

        saleService.cancelSale(
            saleId,
            currentUser
        );

        Payment payment =
        paymentRepository
                .findBySaleId(saleId)
                .orElseThrow();

        assertEquals(
                PaymentStatus.CANCELLED,
                payment.getPaymentStatus()
        );
    }

    @Test
    void cancellation_releases_stock() {

        int before =
                inventory.getReservedQuantity();

        saleService.cancelPayment(
                paymentId
        );

        Inventory updated =
                inventoryRepository
                        .findById(
                        inventory.getId()
                        )
                        .orElseThrow();

        assertEquals(
                before - reservedQty,
                updated.getReservedQuantity()
        );
    }

    @Test
    void total_is_calculated_by_backend() {

        Sale sale =
                saleService.createSale(request);

        assertEquals(
            new BigDecimal("199.98"),
            sale.getTotal()
        );
    }
}
