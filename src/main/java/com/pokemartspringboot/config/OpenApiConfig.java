package com.pokemartspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;

@Configuration
@OpenAPIDefinition(info = @Info(title = "PokeMart API", version = "1"))
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi cartsOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("carts")
                .packagesToScan("com.pokemartspringboot.cart")
                .build();
    }

    @Bean
    public GroupedOpenApi cartItemsOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("cartItems")
                .packagesToScan("com.pokemartspringboot.cartitem")
                .build();
    }

    @Bean
    public GroupedOpenApi productsOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("products")
                .packagesToScan("com.pokemartspringboot.product")
                .build();
    }

    @Bean
    public GroupedOpenApi usersOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("users")
                .packagesToScan("com.pokemartspringboot.user")
                .build();
    }

    @Bean
    public GroupedOpenApi loginOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("login")
                .packagesToScan("com.pokemartspringboot.auth")
                .build();
    }
}
