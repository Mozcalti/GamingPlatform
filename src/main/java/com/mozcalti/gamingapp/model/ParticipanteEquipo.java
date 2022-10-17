package com.mozcalti.gamingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "participante_equipo")
@Data
@NoArgsConstructor
public class ParticipanteEquipo {
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
    private Participantes participantesByIdParticipante;

    @ManyToOne
    @JoinColumn(name = "id_equipo", referencedColumnName = "id_equipo", nullable = false, insertable = false, updatable = false)
    private Equipos equiposByIdEquipo;

    public ParticipanteEquipo(int idParticipante, int idEquipo) {
        this.idParticipante = idParticipante;
        this.idEquipo = idEquipo;
    }

    public ParticipanteEquipo(int idParticipanteEquipo, int idParticipante, int idEquipo) {
        this.idParticipanteEquipo = idParticipanteEquipo;
        this.idParticipante = idParticipante;
        this.idEquipo = idEquipo;
    }

}
