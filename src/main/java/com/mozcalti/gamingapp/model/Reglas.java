package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mozcalti.gamingapp.request.torneo.ReglasRequest;
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
    @Column(name = "id_etapa")
    private Integer idEtapa;
    @OneToOne
    @JoinColumn(name = "id_etapa", referencedColumnName = "id_etapa",insertable = false, updatable = false)
    @JsonIgnore
    private Etapas etapas;

    public Reglas(ReglasRequest reglasRequest, Etapas etapas) {
        this.numCompetidores = reglasRequest.getNumCompetidores();
        this.numRondas = reglasRequest.getNumRondas();
        this.tiempoBatallaAprox = reglasRequest.getTiempoBatallaAprox();
        this.trabajo = reglasRequest.getTrabajo();
        this.tiempoEspera = reglasRequest.getTiempoEspera();
        this.idEtapa = etapas.getIdEtapa();
    }
}
