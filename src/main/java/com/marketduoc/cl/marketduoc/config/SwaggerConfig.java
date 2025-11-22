package com.marketduoc.cl.marketduoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(
            new Info()
            .title("Api Aplicacion MarketDuoc")
            .version("7.7.7")
            .description("Esta API permite gestionar usuarios, productos, categorías y estados de productos en el sistema MarketDuoc")
        );
    }
}
