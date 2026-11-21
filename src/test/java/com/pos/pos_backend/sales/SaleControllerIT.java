package com.pos.pos_backend.sales;

import org.junit.jupiter.api.Test;

public class SaleControllerIT {
    @Test
    void cash_sale_created()
            throws Exception {

        mockMvc.perform(
                post("/api/v1/sales")
                        .header(AUTHORIZATION,
                                bearer(ownerToken()))
                        .contentType(APPLICATION_JSON)
                        .content(cashSaleRequest())
        )
        .andExpect(status().isCreated())
        .andExpect(
                jsonPath("$.status")
                        .value("COMPLETED")
        );
    }

    @Test
    void eft_sale_created()
            throws Exception {

        mockMvc.perform(
                post("/api/v1/sales")
                .header(
                    AUTHORIZATION,
                    bearer(ownerToken())
                )
                .contentType(APPLICATION_JSON)
                .content(eftRequest())
        )
        .andExpect(status().isCreated());

    }
}
