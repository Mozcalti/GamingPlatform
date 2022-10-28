package com.mozcalti.gamingapp.model.dto;

import com.mozcalti.gamingapp.model.Equipos;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class RobotsDTO {

    private final String nombre;
    private final Integer activo;
    private final Integer idEquipo;
    private final Equipos equiposByIdEquipo;
}
