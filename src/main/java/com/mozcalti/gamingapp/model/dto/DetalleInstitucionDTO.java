package com.mozcalti.gamingapp.model.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class DetalleInstitucionDTO {

    private final int id;
    private final String nombre;
    private final String correo;
    private final String fechaCreacion;
    private final String logo;
    private final List<ParticipanteDTO> participantes;
}
