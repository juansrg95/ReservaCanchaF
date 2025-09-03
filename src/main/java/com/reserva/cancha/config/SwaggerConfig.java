
package com.reserva.cancha.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
            .title("API Reservas de Canchas")
            .version("v0.1.0")
            .description("Micro backend - Reservas de canchas (Seminario 2025-2)")
            .contact(new Contact().name("Equipo").email("equipo@example.com")));
    }
}
