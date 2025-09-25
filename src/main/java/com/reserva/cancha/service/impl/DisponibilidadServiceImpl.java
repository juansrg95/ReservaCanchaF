package com.reserva.cancha.service.impl;

import com.reserva.cancha.dto.SlotDTO;
import com.reserva.cancha.model.Reserva;
import com.reserva.cancha.repository.ReservaOverlapRepository;
import com.reserva.cancha.service.DisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadServiceImpl implements DisponibilidadService {

    private final ReservaOverlapRepository overlapRepo;

    @Override
    public List<SlotDTO> calcular(Long canchaId, LocalDateTime from, LocalDateTime to, int slotMin) {
        List<Reserva> reservas = overlapRepo.findAllByCancha(canchaId);

        // Normalizamos a intervalos [inicio, fin]
        record Interval(LocalDateTime start, LocalDateTime end) {}
        List<Interval> intervals = new ArrayList<>();

        for (Reserva r : reservas) {
            // Excluir CANCELADA si existe estado
            String estado = tryGetEstado(r);
            if ("CANCELADA".equalsIgnoreCase(estado)) continue;

            LocalDateTime ini = tryGet(r, "getInicio", "getFechaInicio");
            LocalDateTime fin = tryGet(r, "getFin", "getFechaFin");
            if (ini == null || fin == null) continue;

            // Solo lo que cruza con [from, to)
            if (ini.isBefore(to) && fin.isAfter(from)) {
                intervals.add(new Interval(ini, fin));
            }
        }

        // Generar slots libres
        List<SlotDTO> libres = new ArrayList<>();
        for (LocalDateTime t = from; !t.plusMinutes(slotMin).isAfter(to); t = t.plusMinutes(slotMin)) {
            // Copias efectivamente finales para el lambda
            LocalDateTime slotStart = t;
            LocalDateTime slotEnd   = t.plusMinutes(slotMin);

            boolean ocupado = intervals.stream()
                    .anyMatch(iv -> iv.start().isBefore(slotEnd) && iv.end().isAfter(slotStart));

            if (!ocupado) {
                libres.add(new SlotDTO(slotStart, slotEnd));
            }
        }
        return libres;
        // Fin calcular
    }

    // ---------- helpers por reflection (acepta distintos nombres de campos) ----------
    private static LocalDateTime tryGet(Reserva r, String... getters) {
        for (String g : getters) {
            try {
                Method m = r.getClass().getMethod(g);
                Object v = m.invoke(r);
                if (v instanceof LocalDateTime dt) return dt;
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static String tryGetEstado(Reserva r) {
        try {
            Method m = r.getClass().getMethod("getEstado");
            Object v = m.invoke(r);
            if (v == null) return null;
            if (v instanceof String s) return s;
            try {
                Method name = v.getClass().getMethod("name"); // enum
                return (String) name.invoke(v);
            } catch (Exception ignored) {
                return v.toString();
            }
        } catch (NoSuchMethodException e) {
            return null; // la entidad no tiene estado
        } catch (Exception e) {
            return null;
        }
    }
}

