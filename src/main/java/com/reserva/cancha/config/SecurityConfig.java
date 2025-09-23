package com.reserva.cancha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración central de seguridad.
 *
 * - Exponemos el Health y Swagger sin autenticación (para pruebas y monitoreo).
 * - Permitimos en público los GET de canchas.
 * - Protegemos lo demás con Basic Auth (usuarios y roles vienen de BD).
 * - Reglas explícitas de rol por recurso:
 *      * /api/usuarios/** → solo ADMIN
 *      * /api/canchas/**  → GET público; mutaciones solo ADMIN
 *      * /api/reservas/** → USER o ADMIN
 */
@Configuration
@EnableMethodSecurity // Habilita anotaciones como @PreAuthorize si las quieres usar en servicios/controladores
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Para APIs (sin formularios), desactivamos CSRF (no usamos cookies/sesiones para escribir)
                .csrf(csrf -> csrf.disable())

                // Usaremos autenticación HTTP Basic (usuario/clave en cabecera Authorization)
                .httpBasic(basic -> {})

                // Autorización por rutas:
                .authorizeHttpRequests(auth -> auth

                        // ---- Públicos: health y swagger ----
                        .requestMatchers("/health", "/actuator/health").permitAll()
                        .requestMatchers("/actuator/**", "/error").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // ---- Canchas ----
                        // GET de canchas es público (para catálogo/listados)
                        .requestMatchers(HttpMethod.GET, "/api/canchas/**").permitAll()
                        // Altas/bajas/cambios de canchas solo ADMIN
                        .requestMatchers(HttpMethod.POST,   "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/canchas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/canchas/**").hasRole("ADMIN")

                        // ---- Reservas ----
                        // Toda la API de reservas requiere rol USER o ADMIN
                        .requestMatchers("/api/reservas/**").hasAnyRole("USER", "ADMIN")

                        // ---- Usuarios (administración) ----
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        // Cualquier otra ruta requiere estar autenticado
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}


