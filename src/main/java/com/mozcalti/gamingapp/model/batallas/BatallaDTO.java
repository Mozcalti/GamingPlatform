package com.mozcalti.gamingapp.model.batallas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BatallaDTO {

    private Integer idEtapa;
    private Integer idBatalla;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private List<BatallaParticipanteDTO> batallaParticipantes;
    private Integer rondas;

    public BatallaDTO() {
        this.batallaParticipantes = new ArrayList<>();
    }
}