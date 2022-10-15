package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mozcalti.gamingapp.request.torneo.EtapaRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "etapas")
@Data
@NoArgsConstructor
public class Etapas {
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
    private Collection<EtapaEquipo> etapaEquiposByIdEtapa;

    @ManyToOne
    @JoinColumn(name = "id_torneo", referencedColumnName = "id_torneo", insertable = false, updatable = false)
    @JsonIgnore
    private Torneos torneosByIdTorneo;

    @OneToOne(mappedBy = "etapas")
    private Reglas reglas;

    @OneToMany(mappedBy = "etapasByIdEtapa")
    private Collection<EtapaBatalla> etapaBatallasByIdEtapa;

    public Etapas(EtapaRequest etapaRequest, Torneos torneos) {
        this.numeroEtapa = etapaRequest.getNumeroEtapa();
        this.fechaInicio = etapaRequest.getFechaInicio();
        this.fechaFin = etapaRequest.getFechaFin();
        this.idTorneo = torneos.getIdTorneo();
    }
}
