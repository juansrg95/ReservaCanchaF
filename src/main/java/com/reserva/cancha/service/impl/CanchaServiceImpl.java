package com.reserva.cancha.service.impl;

import com.reserva.cancha.model.Cancha;
import com.reserva.cancha.repository.CanchaRepository;
import com.reserva.cancha.service.CanchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CanchaServiceImpl implements CanchaService {

    private final CanchaRepository canchaRepository;

    @Override
    public List<Cancha> listarActivas() {
        return canchaRepository.findByActivaTrue();
    }
}
