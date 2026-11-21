package com.pos.pos_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.pos_backend.business.entity.Business;
import com.pos.pos_backend.security.UserPrincipal;
import com.pos.pos_backend.user.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TestDataFactory testDataFactory;

    protected Business business;

    protected User owner;

    protected User staff;

    @BeforeEach
    void setupTestData() {

        business =
                testDataFactory.createBusiness(
                        "Test Business"
                );

        owner =
                testDataFactory.createOwner(
                        "owner@test.com",
                        business.getId()
                );

        staff =
                testDataFactory.createStaff(
                        "staff@test.com",
                        business.getId()
                );
    }

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("pos_test")
                    .withUsername("test")
                    .withPassword("test");

    protected static final String AUTHORIZATION = "Authorization";

        protected String bearer(String token) {
            return "Bearer " + token;
        }

        protected String ownerToken() throws Exception {
            return obtainToken(
                "owner@test.com",
                "Password123"
            );
        }

        protected String staffToken() throws Exception {
            return obtainToken(
                "staff@test.com",
                "Password123"
            );
        }

    @DynamicPropertySource
        static void registerProperties(
            DynamicPropertyRegistry registry
        ) {

           registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
           );

           registry.add(
                "spring.datasource.username",
                postgres::getUsername
           );

           registry.add(
                "spring.datasource.password",
                postgres::getPassword
           );
    }

    protected String obtainToken() throws Exception {

        String response = mockMvc.perform(
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
        .andReturn()
        .getResponse()
        .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        return json.get("token").asText();
    }

    protected String obtainToken(
        String email,
        String password
        ) throws Exception {

                String response = mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                        "email":"%s",
                                        "password":"%s"
                                        }
                                        """.formatted(email, password))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

                JsonNode json =
                        objectMapper.readTree(response);

        return json.get("token").asText();
        }

    protected void authenticate(User user) {

        UserPrincipal principal =
                new UserPrincipal(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }
}