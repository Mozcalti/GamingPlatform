package com.mozcalti.gamingapp.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class RobotsDTO {

    private final Integer idRobot;
    private final String nombre;
    private final Integer activo;
    private final Integer idEquipo;
    private final String className;
    private final String tipo;
}
