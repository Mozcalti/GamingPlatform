package com.mozcalti.gamingapp.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RobotsDTO {

    private final String nombre;
    private final Integer activo;
    private final Integer idParticipante;
}
