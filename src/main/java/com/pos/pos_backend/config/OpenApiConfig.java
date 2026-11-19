package com.pos.pos_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {

        final String bearerScheme = "BearerAuth";

        return new OpenAPI()

                .info(
                        new Info()
                                .title("Retail POS API")
                                .version("1.0.0")
                                .description(
                                        "Mobile-first POS and Inventory Management System"
                                )
                                .contact(
                                        new Contact()
                                                .name("Karabo Tebeila")
                                )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(bearerScheme)
                )

                .schemaRequirement(
                        bearerScheme,
                        new SecurityScheme()
                                .name(bearerScheme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}