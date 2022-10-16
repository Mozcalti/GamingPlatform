package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "torneos")
@Data
@NoArgsConstructor
public class Torneos {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_torneo")
    private int idTorneo;
    @Basic
    @Column(name = "fecha_inicio")
    private String fechaInicio;
    @Basic
    @Column(name = "fecha_fin")
    private String fechaFin;
    @Basic
    @Column(name = "num_etapas")
    private Integer numEtapas;

    @OneToMany(mappedBy = "torneosByIdTorneo")
    private Collection<Etapas> etapasByIdTorneo;

    @OneToMany(mappedBy = "horasHabilesByIdTorneo")
    private Collection<TorneoHorasHabiles> torneoHorasHabilesByIdTorneo;

    public Torneos(TorneoDTO torneoDTO) {
        if(torneoDTO.getIdTorneo() != null) {
            this.idTorneo = torneoDTO.getIdTorneo();
        }
        this.fechaInicio = torneoDTO.getFechaInicio();
        this.fechaFin = torneoDTO.getFechaFin();
        this.numEtapas = torneoDTO.getNumEtapas();
    }

}
