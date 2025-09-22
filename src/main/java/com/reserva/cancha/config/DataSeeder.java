package com.reserva.cancha.config;

import com.reserva.cancha.model.Rol;
import com.reserva.cancha.model.Usuario;
import com.reserva.cancha.repository.RolRepository;
import com.reserva.cancha.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder encoder;

    public DataSeeder(UsuarioRepository usuarioRepo, RolRepository rolRepo, PasswordEncoder encoder) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // crea admin si no existe
        usuarioRepo.findByUsername("admin").ifPresentOrElse(
                u -> {}, // ya existe
                () -> {
                    Rol admin = rolRepo.findByNombre("ROLE_ADMIN")
                            .orElseThrow(() -> new IllegalStateException("Falta ROLE_ADMIN (Flyway V2)"));

                    Usuario u = Usuario.builder()
                            .username("admin")
                            .password(encoder.encode("Admin#2025"))
                            .nombre("Administrador")
                            .email("admin@demo.com")
                            .enabled(true)
                            .roles(Set.of(admin))
                            .build();
                    usuarioRepo.save(u);
                }
        );
    }
}

