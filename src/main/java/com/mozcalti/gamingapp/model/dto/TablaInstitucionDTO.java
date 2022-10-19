package com.mozcalti.gamingapp.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class TablaInstitucionDTO {

    private final int id;
    private final String nombre;
    private final String correo;
    private final String fechaCreacion;
    private final String logo;
}
