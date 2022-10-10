package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "etapa_equipo")
public class EtapaEquipoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_etapa_equipo")
    private int idEtapaEquipo;
    @Basic
    @Column(name = "id_etapa")
    private int idEtapa;
    @Basic
    @Column(name = "id_equipo")
    private int idEquipo;
    @ManyToOne
    @JoinColumn(name = "id_etapa", referencedColumnName = "id_etapa", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private EtapasEntity etapasByIdEtapa;
    @ManyToOne
    @JoinColumn(name = "id_equipo", referencedColumnName = "id_equipo", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private EquiposEntity equiposByIdEquipo;

    public int getIdEtapaEquipo() {
        return idEtapaEquipo;
    }

    public void setIdEtapaEquipo(int idEtapaEquipo) {
        this.idEtapaEquipo = idEtapaEquipo;
    }

    public int getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(int idEtapa) {
        this.idEtapa = idEtapa;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtapaEquipoEntity that = (EtapaEquipoEntity) o;
        return idEtapaEquipo == that.idEtapaEquipo && idEtapa == that.idEtapa && idEquipo == that.idEquipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtapaEquipo, idEtapa, idEquipo);
    }

    public EtapasEntity getEtapasByIdEtapa() {
        return etapasByIdEtapa;
    }

    public void setEtapasByIdEtapa(EtapasEntity etapasByIdEtapa) {
        this.etapasByIdEtapa = etapasByIdEtapa;
    }

    public EquiposEntity getEquiposByIdEquipo() {
        return equiposByIdEquipo;
    }

    public void setEquiposByIdEquipo(EquiposEntity equiposByIdEquipo) {
        this.equiposByIdEquipo = equiposByIdEquipo;
    }
}
