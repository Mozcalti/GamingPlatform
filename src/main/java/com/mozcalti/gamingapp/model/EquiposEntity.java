package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "equipos")
public class EquiposEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_equipo")
    private int idEquipo;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "equiposByIdEquipo")
    private Collection<EtapaEquipoEntity> etapaEquiposByIdEquipo;
    @OneToMany(mappedBy = "equiposByIdEquipo")
    private Collection<ParticipanteEquipoEntity> participanteEquiposByIdEquipo;

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquiposEntity that = (EquiposEntity) o;
        return idEquipo == that.idEquipo && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo, nombre);
    }

    public Collection<EtapaEquipoEntity> getEtapaEquiposByIdEquipo() {
        return etapaEquiposByIdEquipo;
    }

    public void setEtapaEquiposByIdEquipo(Collection<EtapaEquipoEntity> etapaEquiposByIdEquipo) {
        this.etapaEquiposByIdEquipo = etapaEquiposByIdEquipo;
    }

    public Collection<ParticipanteEquipoEntity> getParticipanteEquiposByIdEquipo() {
        return participanteEquiposByIdEquipo;
    }

    public void setParticipanteEquiposByIdEquipo(Collection<ParticipanteEquipoEntity> participanteEquiposByIdEquipo) {
        this.participanteEquiposByIdEquipo = participanteEquiposByIdEquipo;
    }
}
