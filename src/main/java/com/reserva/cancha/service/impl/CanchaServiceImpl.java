package com.reserva.cancha.service.impl;

import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.repository.CanchaRepository;
import com.reserva.cancha.service.CanchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CanchaServiceImpl implements CanchaService {

    private final CanchaRepository repo;

    @Override
    public List<Cancha> listarActivas() {
        // Opción A: usa el query method (recomendado)
        return repo.findByActivaTrue();

        // Opción B (si no quieres el método en el repo) — comenta A y descomenta B
        // return repo.findAll().stream()
        //         .filter(c -> Boolean.TRUE.equals(c.getActiva()))
        //         .toList();
    }

    @Override
    public Cancha crear(Cancha cancha) {
        cancha.setId(null);  // evita update accidental
        if (cancha.getActiva() == null) {
            cancha.setActiva(Boolean.TRUE);
        }
        return repo.save(cancha);
    }

    @Override
    public Cancha actualizar(Long id, Cancha cancha) {
        Cancha db = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cancha no encontrada"));
        db.setNombre(cancha.getNombre());
        db.setDeporte(cancha.getDeporte());
        db.setActiva(cancha.getActiva());   // <-- usa getActiva()
        db.setSede(cancha.getSede());
        return repo.save(db);
    }

    @Override
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Cancha no encontrada");
        }
        repo.deleteById(id);
    }
}

