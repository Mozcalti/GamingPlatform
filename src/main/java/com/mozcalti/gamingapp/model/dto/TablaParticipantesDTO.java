package com.mozcalti.gamingapp.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TablaParticipantesDTO {
    private final int idParticipante;
    private final String nombre;
    private final String apellidos;
    private final String correo;
    private final String academia;
    private final String ies;
    private final String carrera;
    private final Integer semestre;
    private final String foto;
    private final String fechaCreacion;
    private final Integer idInstitucion;
}
