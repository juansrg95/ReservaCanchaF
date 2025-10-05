package com.reserva.cancha.repository;

import com.reserva.cancha.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Reserva.
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /** Todas las reservas de una cancha ordenadas por inicio ASC */
    List<Reserva> findByCancha_IdOrderByInicioAsc(Long canchaId);

    /** Todas las reservas entre dos instantes, ordenadas por inicio ASC */
    List<Reserva> findByInicioBetweenOrderByInicioAsc(LocalDateTime desde, LocalDateTime hasta);

    /** Reservas de una cancha entre dos instantes, ordenadas por inicio ASC */
    List<Reserva> findByCancha_IdAndInicioBetweenOrderByInicioAsc(Long canchaId,
                                                                  LocalDateTime desde,
                                                                  LocalDateTime hasta);

    /**
     * ¿Existe choque de horario en la misma cancha?
     * (nuevo.inicio < existente.fin) AND (nuevo.fin > existente.inicio)
     *
     * ⚙️ Versión JPQL: compatible con PostgreSQL y H2 (para tests)
     */
    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM Reserva r
            WHERE r.cancha.id = :canchaId
              AND (:inicio < r.fin AND :fin > r.inicio)
            """)
    boolean existeChoque(@Param("canchaId") Long canchaId,
                         @Param("inicio") LocalDateTime inicio,
                         @Param("fin") LocalDateTime fin);
}



