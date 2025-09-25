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
                .csrf(csrf -> csrf.disable()) // desactivar CSRF
                .cors(Customizer.withDefaults()) // usar la config de CorsConfig
                .httpBasic(Customizer.withDefaults()) // Basic Auth
                .authorizeHttpRequests(auth -> auth
                        // ⚠️ Importante: permitir OPTIONS (preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

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

                        // Cualquier otra request → autenticada
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}




