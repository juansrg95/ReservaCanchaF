
package com.reserva.cancha.repository;

import com.reserva.cancha.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("""

    select r from Reserva r
    where r.cancha.id = :canchaId
      and (r.inicio < :fin and r.fin > :inicio)
      and r.estado = 'ACTIVA'
    """)
    List<Reserva> solapes(@Param("canchaId") Long canchaId,
                          @Param("inicio") Instant inicio,
                          @Param("fin") Instant fin);
}
