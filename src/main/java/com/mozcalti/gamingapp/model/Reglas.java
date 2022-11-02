package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mozcalti.gamingapp.model.torneos.ReglasDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "reglas")
@Data
@NoArgsConstructor
public class Reglas {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_regla")
    private int idRegla;
    @Basic
    @Column(name = "num_competidores")
    private Integer numCompetidores;
    @Basic
    @Column(name = "num_rondas")
    private Integer numRondas;

    @Basic
    @Column(name = "tiempo_batalla_aprox")
    private Integer tiempoBatallaAprox;

    @Basic
    @Column(name = "trabajo")
    private String trabajo;

    @Basic
    @Column(name = "tiempo_espera")
    private Integer tiempoEspera;

    @Basic
    @Column(name = "arena_ancho")
    private Integer arenaAncho;

    @Basic
    @Column(name = "arena_alto")
    private Integer arenaAlto;

    @Basic
    @Column(name = "id_etapa")
    private Integer idEtapa;

    @OneToOne
    @JoinColumn(name = "id_etapa", referencedColumnName = "id_etapa",insertable = false, updatable = false)
    @JsonIgnore
    private Etapas etapas;

    public Reglas(ReglasDTO reglasDTO, Integer idEtapa) {
        if(reglasDTO.getIdRegla() != null) {
            this.idRegla = reglasDTO.getIdRegla();
        }
        this.numCompetidores = reglasDTO.getNumCompetidores();
        this.numRondas = reglasDTO.getNumRondas();
        this.tiempoBatallaAprox = reglasDTO.getTiempoBatallaAprox();
        this.trabajo = reglasDTO.getTrabajo();
        this.tiempoEspera = reglasDTO.getTiempoEspera();
        this.idEtapa = idEtapa;
    }
}
