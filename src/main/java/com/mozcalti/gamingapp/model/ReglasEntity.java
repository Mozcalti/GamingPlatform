package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mozcalti.gamingapp.request.ReglasRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "reglas")
@Data
@NoArgsConstructor
public class ReglasEntity {
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
    @Column(name = "id_etapa")
    private Integer idEtapa;
    @OneToOne
    @JoinColumn(name = "id_etapa", referencedColumnName = "id_etapa",insertable = false, updatable = false)
    @JsonIgnore
    private EtapasEntity etapas;

    public ReglasEntity(ReglasRequest reglasRequest, EtapasEntity etapasEntity) {
        this.numCompetidores = reglasRequest.getNumCompetidores();
        this.numRondas = reglasRequest.getNumRondas();
        this.idEtapa = etapasEntity.getIdEtapa();
    }
}
