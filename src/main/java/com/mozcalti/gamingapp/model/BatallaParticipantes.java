package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mozcalti.gamingapp.response.batalla.ParticipanteResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "batalla_participantes")
@Data
@NoArgsConstructor
public class BatallaParticipantes {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_batalla_participante")
    private int idBatallaParticipante;

    @Basic
    @Column(name = "id_participante_equipo")
    private int idParticipanteEquipo;

    @Basic
    @Column(name = "nombre")
    private String nombre;

    @Basic
    @Column(name = "id_batalla")
    private Integer idBatalla;

    @ManyToOne
    @JoinColumn(name = "id_batalla", referencedColumnName = "id_batalla", insertable = false, updatable = false)
    @JsonIgnore
    private Batallas batallasByIdBatalla;

    public BatallaParticipantes(ParticipanteResponse participanteResponse, Integer idBatalla) {
        this.idParticipanteEquipo = participanteResponse.getIdParticipante();
        this.nombre = participanteResponse.getNombre();
        this.idBatalla = idBatalla;
    }

}
