package com.pos.pos_backend.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.pos_backend.analytics.repository.AnalyticsRepository;
import com.pos.pos_backend.config.AbstractIntegrationTest;
import com.pos.pos_backend.product.entity.Product;
import com.pos.pos_backend.product.repository.ProductRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

public class BusinessIsolationIT extends AbstractIntegrationTest{
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected AnalyticsRepository analyticsRepository;

    /*@Test
    void business_a_cannot_see_business_b_products()
            throws Exception {

        String tokenA = businessAOwnerToken();

        mockMvc.perform(
                get("/api/v1/products")
                        .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + tokenA
                        )
        )
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$[*].businessId",
                everyItem(is(1)))
        );
    }

    @Test
    void repository_filters_by_business() {

        Optional<Product> result =
                productRepository
                        .findByIdAndBusinessId(
                                productBId,
                                businessAId
                        );

        assertTrue(
                result.isEmpty()
        );
    }

    @Test
    void revenue_ignores_pending_payments() {

        BigDecimal revenue =
                analyticsRepository
                .calculateRevenue(
                    businessId
                );

        assertEquals(
            new BigDecimal("1500.00"),
            revenue
        );
    }*/
}
