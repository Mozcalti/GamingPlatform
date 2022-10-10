package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.request.TorneoRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "torneos")
@Data
@NoArgsConstructor
public class TorneosEntity {
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
    private Collection<EtapasEntity> etapasByIdTorneo;

    public TorneosEntity(TorneoRequest torneoRequest) {
        this.fechaInicio = torneoRequest.getFechaInicio();
        this.fechaFin = torneoRequest.getFechaFin();
        this.numEtapas = torneoRequest.getNumEtapas();
    }
}
