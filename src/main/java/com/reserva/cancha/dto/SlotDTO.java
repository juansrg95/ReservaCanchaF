package com.reserva.cancha.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SlotDTO {
    private LocalDateTime inicio;
    private LocalDateTime fin;
}

