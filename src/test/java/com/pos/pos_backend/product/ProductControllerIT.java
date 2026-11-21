package com.pos.pos_backend.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.pos.pos_backend.business.entity.Business;
import com.pos.pos_backend.category.Category;
import com.pos.pos_backend.config.AbstractIntegrationTest;
import com.pos.pos_backend.config.TestDataFactory;
import com.pos.pos_backend.product.entity.Product;
import com.pos.pos_backend.product.entity.ProductVariant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerIT extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataFactory testDataFactory;

    @Test
    void create_product_success()
            throws Exception {

        mockMvc.perform(
                post("/api/v1/products")
                .header(AUTHORIZATION, bearer(ownerToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name":"Coke"
                    }
                """)
        )
        .andExpect(status().isCreated());
    }

    @Test
    void create_variant_success()
            throws Exception {

        Business business =
                testDataFactory.createBusiness(
                        "Test Business"
                );

        Category category =
                testDataFactory.createCategory(
                        business,
                        "Beverages"
                );

        Product product =
                testDataFactory.createProduct(
                        business,
                        category,
                        "Coke"
                );

        mockMvc.perform(
                post("/api/v1/products/%d/variants"
                        .formatted(
                                product.getId()
                        ))
        )
        .andExpect(status().isCreated());
    }

    @Test
    void product_name_required()
            throws Exception {

        mockMvc.perform(
                post("/api/v1/products")
                        .header(
                            AUTHORIZATION,
                            bearer(ownerToken())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "name":""
                        }
                        """)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void stock_in_success()
            throws Exception {

        ProductVariant variant =
        testDataFactory
                .createInventoryWithStock(
                        10
                );

        mockMvc.perform(
                post("/api/v1/inventory/stock-in")
                        .header(
                        AUTHORIZATION,
                        bearer(ownerToken())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        "variantId":%d,
                        "quantity":50
                        }
                        """)
        )
        .andExpect(status().isOk());
    }

    @Test
    void inventory_adjustment_success()
            throws Exception {

        mockMvc.perform(
                post("/api/v1/inventory/adjust")
        )
        .andExpect(status().isOk());
    }

    @Test
    void insufficient_stock_rejected()
            throws Exception {
        ProductVariant variant =
        testDataFactory
                .createInventoryWithStock(
                        10
                );

        mockMvc.perform(
                post("/api/v1/sales")
                        .header(
                            AUTHORIZATION,
                            bearer(ownerToken())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "paymentMethod":"CASH",
                        "items":[
                            {
                                "variantId":%d,
                                "quantity":9999
                            }
                        ]
                        }
                        """)
        )
        .andExpect(status().isBadRequest());
    }
}
