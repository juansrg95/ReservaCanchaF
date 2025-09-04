package com.reserva.cancha.controller;

import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.service.CanchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
public class CanchaController {

    private final CanchaService canchaService;

    @GetMapping("/activas")
    public List<Cancha> activas() {
        return canchaService.listarActivas();
    }
}

