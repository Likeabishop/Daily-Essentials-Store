package com.pos.pos_backend.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.pos.pos_backend.config.AbstractIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthorizationIT
        extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void owner_can_confirm_eft()
            throws Exception {

        String ownerToken = ownerToken();

        mockMvc.perform(
                post("/api/v1/payments/1/confirm")
                        .header(
                           AUTHORIZATION,
                           bearer(ownerToken)
                        )
        )
        .andExpect(status().isOk());
    }

    @Test
    void staff_cannot_confirm_eft()
            throws Exception {

        String staffToken = staffToken();

        mockMvc.perform(
                post("/api/v1/payments/1/confirm")
                        .header(
                            AUTHORIZATION,
                            bearer(staffToken)
                        )
        )
        .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticated_access_denied()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/analytics/revenue")
        )
        .andExpect(status().isUnauthorized());
    }
}