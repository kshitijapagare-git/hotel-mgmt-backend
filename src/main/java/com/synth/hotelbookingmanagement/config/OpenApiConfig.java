package com.synth.hotelbookingmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("hotel-booking-management API")
                        .version("v1")
                        .description("REST API for hotel-booking-management"))
                .addServersItem(new Server().url("/api/v1").description("v1"));
    }
}
