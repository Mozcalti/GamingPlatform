package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.model.batallas.BatallaParticipanteDTO;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_batalla", referencedColumnName = "id_batalla", insertable = false, updatable = false)
    private Batallas batallasByIdBatalla;

    public BatallaParticipantes(BatallaParticipanteDTO batallaParticipanteDTO, Integer idBatalla) {
        this.idParticipanteEquipo = batallaParticipanteDTO.getIdParticipante();
        this.nombre = batallaParticipanteDTO.getNombre();
        this.idBatalla = idBatalla;
    }

}
