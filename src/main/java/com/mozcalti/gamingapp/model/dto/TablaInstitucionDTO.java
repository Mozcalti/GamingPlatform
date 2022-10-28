package com.mozcalti.gamingapp.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Getter
public class TablaInstitucionDTO {

    private final int id;
    private final String nombre;
    private final String correo;
    private final LocalDateTime fechaCreacion;
    private final String logo;
}
