package com.reserva.cancha.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void healthCheck_returns_ok_message() throws Exception {
        mvc.perform(get("/health"))
                .andExpect(status().isOk())
                // Si quieres exigir el texto exacto:
                .andExpect(content().string("Backend OK - Rama feature-juanruizg"));

        // Alternativa más flexible (si el texto podría cambiar un poco):
        // .andExpect(content().string(org.hamcrest.Matchers.containsString("Backend OK")));
    }
}
