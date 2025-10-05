package com.reserva.cancha.repository;

import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.model.Reserva;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ReservaRepositoryIT {

    @Autowired private ReservaRepository reservaRepository;
    @Autowired private CanchaRepository canchaRepository;

    // ----- Helpers SIN getters/setters -----
    private static void setField(Object target, String name, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unchecked")
    private static <T> T getField(Object target, String name, Class<T> type) {
        try {
            Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // ---------------------------------------

    @Test
    void existeChoque_contiguoNoSolapa_ySolapaTrue() {
        // Cancha
        Cancha c = new Cancha();
        setField(c, "nombre", "Cancha IT");
        c = canchaRepository.save(c);
        Long canchaId = getField(c, "id", Long.class); // sin getter

        // Reserva existente 10:00–11:00
        Reserva r = new Reserva();
        setField(r, "cancha", c);
        setField(r, "usuario", "tester");
        setField(r, "inicio", LocalDateTime.of(2025, 10, 5, 10, 0));
        setField(r, "fin",    LocalDateTime.of(2025, 10, 5, 11, 0));
        setField(r, "estado", "ACTIVA");
        reservaRepository.save(r);

        // Contiguo 11:00–12:00 → NO hay choque
        boolean contiguo = reservaRepository.existeChoque(
                canchaId,
                LocalDateTime.of(2025, 10, 5, 11, 0),
                LocalDateTime.of(2025, 10, 5, 12, 0)
        );
        assertThat(contiguo).isFalse();

        // Solapa 10:30–11:30 → SÍ hay choque
        boolean solapa = reservaRepository.existeChoque(
                canchaId,
                LocalDateTime.of(2025, 10, 5, 10, 30),
                LocalDateTime.of(2025, 10, 5, 11, 30)
        );
        assertThat(solapa).isTrue();
    }
}





