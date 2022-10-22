package com.mozcalti.gamingapp.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;

@RequiredArgsConstructor
@Getter
public class TablaRobotsDTO {
    private final int idRobot;
    private final String nombre;
    private final Integer activo;
    private final Integer idParticipante;
}
