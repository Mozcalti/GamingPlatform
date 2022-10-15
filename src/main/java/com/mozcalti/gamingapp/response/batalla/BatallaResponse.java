package com.mozcalti.gamingapp.response.batalla;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BatallaResponse {

    private Integer idEtapa;
    private Integer idBatalla;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private List<ParticipanteResponse> participantes;
    private Integer rondas;

    public BatallaResponse() {
        this.participantes = new ArrayList<>();
    }
}
