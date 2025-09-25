package com.reserva.cancha.repository;

import com.reserva.cancha.model.Reserva;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ReservaOverlapRepository {

    @PersistenceContext
    private EntityManager em;

    // Trae todas las reservas de una cancha; el filtrado de solapes lo hacemos en servicio
    public List<Reserva> findAllByCancha(Long canchaId) {
        return em.createQuery("""
                SELECT r FROM Reserva r
                WHERE r.cancha.id = :canchaId
                """, Reserva.class)
                .setParameter("canchaId", canchaId)
                .getResultList();
    }
}


