package com.reserva.cancha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Para API sin sesiones/formularios
                .csrf(csrf -> csrf.disable())
                // Habilita CORS (se toma del CorsConfig de abajo)
                .cors(Customizer.withDefaults())
                // Auth básica
                .httpBasic(Customizer.withDefaults())
                // Autorización por rutas
                .authorizeHttpRequests(auth -> auth
                        // Salud / Swagger públicos
                        .requestMatchers("/health", "/actuator/health").permitAll()
                        .requestMatchers("/actuator/**", "/error").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Canchas: GET público, mutaciones ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/canchas/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/canchas/**").hasRole("ADMIN")

                        // Reservas: USER o ADMIN
                        .requestMatchers("/api/reservas/**").hasAnyRole("USER", "ADMIN")

                        // Usuarios: solo ADMIN
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        // Cualquier otra
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}




