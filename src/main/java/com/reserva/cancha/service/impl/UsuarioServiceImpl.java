package com.reserva.cancha.service.impl;

import com.reserva.cancha.dto.UsuarioRequest;
import com.reserva.cancha.dto.UsuarioResponse;
import com.reserva.cancha.model.Rol;
import com.reserva.cancha.model.Usuario;
import com.reserva.cancha.repository.RolRepository;
import com.reserva.cancha.repository.UsuarioRepository;
import com.reserva.cancha.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder encoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepo,
                              RolRepository rolRepo,
                              PasswordEncoder encoder) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
        this.encoder = encoder;
    }

    @Override
    public UsuarioResponse registrarUsuarioNormal(UsuarioRequest req) {
        if (usuarioRepo.existsByUsername(req.username())) {
            throw new IllegalArgumentException("El username ya existe");
        }
        if (req.email() != null && usuarioRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("El email ya existe");
        }

        Rol rolUser = rolRepo.findByNombre("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER no existe (flyway V2)"));

        Usuario u = Usuario.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .nombre(req.nombre())
                .email(req.email())
                .enabled(true)
                .roles(Set.of(rolUser))
                .build();

        Usuario saved = usuarioRepo.save(u);
        return toDto(saved);
    }

    @Override
    public List<UsuarioResponse> listar() {
        return usuarioRepo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public UsuarioResponse obtener(Long id) {
        return usuarioRepo.findById(id).map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepo.deleteById(id);
    }

    private UsuarioResponse toDto(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getUsername(),
                u.getNombre(),
                u.getEmail(),
                u.getRoles().stream().map(Rol::getNombre).collect(java.util.stream.Collectors.toSet())
        );
    }
}

