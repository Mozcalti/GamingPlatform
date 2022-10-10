package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mozcalti.gamingapp.request.EtapaRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "etapas")
@Data
@NoArgsConstructor
public class EtapasEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_etapa")
    private int idEtapa;
    @Basic
    @Column(name = "numero_etapa")
    private int numeroEtapa;
    @Basic
    @Column(name = "fecha_inicio")
    private String fechaInicio;
    @Basic
    @Column(name = "fecha_fin")
    private String fechaFin;
    @Basic
    @Column(name = "id_torneo")
    private Integer idTorneo;
    @OneToMany(mappedBy = "etapasByIdEtapa", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<EtapaEquipoEntity> etapaEquiposByIdEtapa;
    @ManyToOne
    @JoinColumn(name = "id_torneo", referencedColumnName = "id_torneo", insertable = false, updatable = false)
    @JsonIgnore
    private TorneosEntity torneosByIdTorneo;

    @OneToOne(mappedBy = "etapas")
    private ReglasEntity reglasEntity;

    public EtapasEntity(EtapaRequest etapaRequest, TorneosEntity torneosEntity) {
        this.numeroEtapa = etapaRequest.getNumeroEtapa();
        this.fechaInicio = etapaRequest.getFechaInicio();
        this.fechaFin = etapaRequest.getFechaFin();
        this.idTorneo = torneosEntity.getIdTorneo();
    }
}
