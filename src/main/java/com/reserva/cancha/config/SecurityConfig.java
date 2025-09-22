
package com.reserva.cancha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/health", "/actuator/health").permitAll()
                        .requestMatchers("/actuator/**", "/error",
                                "/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()

                        // GET públicos de canchas
                        .requestMatchers(HttpMethod.GET, "/api/canchas/**").permitAll()

                        // lo demás con auth
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}


