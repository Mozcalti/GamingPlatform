package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reglas")
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

    public int getIdRegla() {
        return idRegla;
    }

    public void setIdRegla(int idRegla) {
        this.idRegla = idRegla;
    }

    public Integer getNumCompetidores() {
        return numCompetidores;
    }

    public void setNumCompetidores(Integer numCompetidores) {
        this.numCompetidores = numCompetidores;
    }

    public Integer getNumRondas() {
        return numRondas;
    }

    public void setNumRondas(Integer numRondas) {
        this.numRondas = numRondas;
    }

    public Integer getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(Integer idEtapa) {
        this.idEtapa = idEtapa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReglasEntity that = (ReglasEntity) o;
        return idRegla == that.idRegla && Objects.equals(numCompetidores, that.numCompetidores) && Objects.equals(numRondas, that.numRondas) && Objects.equals(idEtapa, that.idEtapa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRegla, numCompetidores, numRondas, idEtapa);
    }

    public EtapasEntity getEtapas() {
        return etapas;
    }

    public void setEtapas(EtapasEntity etapas) {
        this.etapas = etapas;
    }
}
