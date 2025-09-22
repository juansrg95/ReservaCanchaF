package com.reserva.cancha.service.impl;

import com.reserva.cancha.model.Rol;
import com.reserva.cancha.model.Usuario;
import com.reserva.cancha.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = mapRoles(usuario.getRoles());

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())   // ¡ya debe venir encriptado!
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.isEnabled())
                .build();
    }

    private List<GrantedAuthority> mapRoles(Collection<Rol> roles) {
        if (roles == null || roles.isEmpty()) return List.of();
        return roles.stream()
                .map(r -> {
                    String name = r.getNombre();
                    if (name == null || name.isBlank()) name = "USER";
                    if (!name.startsWith("ROLE_")) name = "ROLE_" + name.toUpperCase();
                    return new SimpleGrantedAuthority(name);
                })
                .collect(Collectors.toList());
    }
}
