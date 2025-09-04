
package com.reserva.cancha.repository;

import com.reserva.cancha.model.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {

    // devuelve todas las canchas activas
    List<Cancha> findByActivaTrue();
}
