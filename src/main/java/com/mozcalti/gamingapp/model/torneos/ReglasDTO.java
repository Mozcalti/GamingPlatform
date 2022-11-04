package com.mozcalti.gamingapp.model.torneos;

import com.mozcalti.gamingapp.model.Reglas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReglasDTO {

    private Integer idRegla;
    private Integer numCompetidores;
    private Integer numRondas;
    private Integer tiempoBatallaAprox;
    private String trabajo;
    private Integer tiempoEspera;
    private Integer arenaAncho;
    private Integer arenaAlto;

    public ReglasDTO(Reglas reglas) {
        this.idRegla = reglas.getIdRegla();
        this.numCompetidores = reglas.getNumCompetidores();
        this.numRondas = reglas.getNumRondas();
        this.tiempoBatallaAprox = reglas.getTiempoBatallaAprox();
        this.trabajo = reglas.getTrabajo();
        this.tiempoEspera = reglas.getTiempoEspera();
        this.arenaAncho = reglas.getArenaAncho();
        this.arenaAlto = reglas.getArenaAlto();
    }
}
