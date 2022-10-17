package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
