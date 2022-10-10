package com.mozcalti.gamingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaMundo {

    @GetMapping("/mensaje")
    public String getMensaje() {
        return "Hola Mundo";
    }

}
