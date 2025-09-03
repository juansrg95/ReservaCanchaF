
package com.reserva.cancha.dto;

import java.time.Instant;

public record ReservaDTO(Long canchaId, String usuario, Instant inicio, Instant fin) {}
