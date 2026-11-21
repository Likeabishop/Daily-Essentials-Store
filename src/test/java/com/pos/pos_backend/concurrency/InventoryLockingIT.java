package com.pos.pos_backend.concurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

import com.pos.pos_backend.business.entity.Business;
import com.pos.pos_backend.category.Category;
import com.pos.pos_backend.category.repository.CategoryRepository;
import com.pos.pos_backend.config.AbstractIntegrationTest;
import com.pos.pos_backend.config.TestDataFactory;
import com.pos.pos_backend.inventory.entity.Inventory;
import com.pos.pos_backend.inventory.repository.InventoryRepository;
import com.pos.pos_backend.payment.enums.PaymentMethod;
import com.pos.pos_backend.product.entity.Product;
import com.pos.pos_backend.product.entity.ProductVariant;
import com.pos.pos_backend.product.repository.ProductRepository;
import com.pos.pos_backend.product.repository.ProductVariantRepository;
import com.pos.pos_backend.sale.dto.CreateSaleItemRequest;
import com.pos.pos_backend.sale.dto.CreateSaleRequest;
import com.pos.pos_backend.sale.service.SaleService;
import com.pos.pos_backend.user.entity.User;
import com.pos.pos_backend.user.repository.UserRepository;

@SpringBootTest
class InventoryLockingIT extends AbstractIntegrationTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private UserRepository userRepository;

    @Test
    void no_overselling_under_concurrency() throws Exception {

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        CountDownLatch latch =
                new CountDownLatch(2);

        Long variantId =
            testDataFactory
                .createInventoryWithStock(10).getId();

        CreateSaleRequest request =
                new CreateSaleRequest(
                        List.of(
                                new CreateSaleItemRequest(
                                        variantId,
                                        8
                                )
                        ),
                        PaymentMethod.CASH
                );

        Runnable task = () -> {

            try {

                authenticateTestOwner();

                saleService.createSale(
                        request,
                        UUID.randomUUID().toString()
                );

                successCount.incrementAndGet();

            } catch (Exception ex) {

                failureCount.incrementAndGet();

            } finally {

                SecurityContextHolder.clearContext();

                latch.countDown();
            }
        };

        executor.submit(task);
        executor.submit(task);

        latch.await();

        Inventory inventory =
                inventoryRepository
                        .findByVariant_Id(variantId)
                        .orElseThrow();

        assertEquals(
                1,
                successCount.get()
        );

        assertEquals(
                1,
                failureCount.get()
        );

        assertEquals(
                2,
                inventory.getQuantityOnHand()
        );
    }

    protected void authenticateTestOwner() {

        User owner =
                userRepository
                        .findByEmail("owner@test.com")
                        .orElseThrow();

        authenticate(owner);
}

    
}