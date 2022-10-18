package com.mozcalti.gamingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "participantes")
@Data
@NoArgsConstructor
public class Participantes {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_participante")
    private int idParticipante;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "apellidos")
    private String apellidos;
    @Basic
    @Column(name = "correo")
    private String correo;
    @Basic
    @Column(name = "carrera")
    private String carrera;
    @Basic
    @Column(name = "foto")
    private byte[] foto;
    @Basic
    @Column(name = "academia")
    private String academia;
    @Basic
    @Column(name = "semestre")
    private Integer semestre;
    @Basic
    @Column(name = "id_institucion")
    private Integer idInstitucion;

    @OneToMany(mappedBy = "participantesByIdParticipante")
    private Collection<ParticipanteEquipo> participanteEquiposByIdParticipante;

}
