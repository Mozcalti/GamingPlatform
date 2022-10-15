package com.mozcalti.gamingapp.request.torneo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReglasRequest {

    private Integer numCompetidores;
    private Integer numRondas;
    private Integer tiempoBatallaAprox;
    private String trabajo;
    private Integer tiempoEspera;

}
