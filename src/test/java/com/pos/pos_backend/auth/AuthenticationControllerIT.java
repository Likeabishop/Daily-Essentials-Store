package com.pos.pos_backend.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.pos.pos_backend.config.AbstractIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIT
        extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_success() throws Exception {

        mockMvc.perform(
                post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                      "email":"owner@test.com",
                      "password":"Password123"
                    }
                    """)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_invalid_credentials() throws Exception {

        mockMvc.perform(
                post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                      "email":"owner@test.com",
                      "password":"wrong"
                    }
                    """)
        )
        .andExpect(status().isUnauthorized());
    }

    @Test
    void current_user_endpoint_returns_profile()
            throws Exception {

        String token = obtainToken();

        mockMvc.perform(
                get("/api/v1/auth/me")
                        .header(
                           HttpHeaders.AUTHORIZATION,
                           "Bearer " + token
                        )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").exists());
    }
}