package com.mozcalti.gamingapp.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Disabled("Se deshabilita por falta de una base de datos de prueba")
    void testAuthentication() throws Exception {
        this.mockMvc.
                perform(post("/api/login").content("{\"username\":\"admin\",\"password\":\"admin\"}").
                        header(HttpHeaders.CONTENT_TYPE, "application/json")).
                andDo(print())
                .andExpect(status().isOk());
    }
}