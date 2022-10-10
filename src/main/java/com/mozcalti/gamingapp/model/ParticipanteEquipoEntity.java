package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "participante_equipo")
public class ParticipanteEquipoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_participante_equipo")
    private int idParticipanteEquipo;
    @Basic
    @Column(name = "id_participante")
    private int idParticipante;
    @Basic
    @Column(name = "id_equipo")
    private int idEquipo;
    @ManyToOne
    @JoinColumn(name = "id_participante", referencedColumnName = "id_participante", nullable = false, insertable = false, updatable = false)
    private ParticipantesEntity participantesByIdParticipante;
    @ManyToOne
    @JoinColumn(name = "id_equipo", referencedColumnName = "id_equipo", nullable = false, insertable = false, updatable = false)
    private EquiposEntity equiposByIdEquipo;

    public int getIdParticipanteEquipo() {
        return idParticipanteEquipo;
    }

    public void setIdParticipanteEquipo(int idParticipanteEquipo) {
        this.idParticipanteEquipo = idParticipanteEquipo;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
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
        ParticipanteEquipoEntity that = (ParticipanteEquipoEntity) o;
        return idParticipanteEquipo == that.idParticipanteEquipo && idParticipante == that.idParticipante && idEquipo == that.idEquipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idParticipanteEquipo, idParticipante, idEquipo);
    }

    public ParticipantesEntity getParticipantesByIdParticipante() {
        return participantesByIdParticipante;
    }

    public void setParticipantesByIdParticipante(ParticipantesEntity participantesByIdParticipante) {
        this.participantesByIdParticipante = participantesByIdParticipante;
    }

    public EquiposEntity getEquiposByIdEquipo() {
        return equiposByIdEquipo;
    }

    public void setEquiposByIdEquipo(EquiposEntity equiposByIdEquipo) {
        this.equiposByIdEquipo = equiposByIdEquipo;
    }
}
