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
                .csrf(csrf -> csrf.disable())
                .cors(c -> {})   // usa el CorsConfigurationSource de arriba
                .httpBasic(c -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/health", "/actuator/health").permitAll()
                        .requestMatchers("/actuator/**", "/error").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/canchas/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST,   "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT,    "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH,  "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers("/api/reservas/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );


        return http.build();
    }
}




