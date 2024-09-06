package com.amenities.dailyaid.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customerOpenAPI(){
        return new OpenAPI().info(
                new Info()
                        .title("Daily Amenities API")
                        .version("1.0.0")
                        .description("API documentation for the Daily Amenities application")
        );
    }
}
