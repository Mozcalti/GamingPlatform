package com.mozcalti.gamingapp.model.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipanteDTO {

    private final String nombre;
    private final String apellidos;
    private final String correo;
    private final String academia;
    private final String ies;
    private final String carrera;
    private final Integer semestre;
    private final Integer idInstitucion;
}
