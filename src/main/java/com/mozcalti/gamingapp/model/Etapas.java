package com.mozcalti.gamingapp.model;

import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
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

    @OneToMany(mappedBy = "etapasByIdEtapa")
    private Collection<EtapaEquipo> etapaEquiposByIdEtapa;

    @OneToOne(mappedBy = "etapas")
    private Reglas reglas;

    @OneToMany(mappedBy = "etapasByIdEtapa")
    private Collection<EtapaBatalla> etapaBatallasByIdEtapa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_torneo", referencedColumnName = "id_torneo", insertable = false, updatable = false)
    private Torneos torneosByIdTorneo;

    public Etapas(EtapaDTO etapaDTO, Integer idTorneo) {
        if(etapaDTO.getIdEtapa() != null) {
            this.idEtapa = etapaDTO.getIdEtapa();
        }
        this.numeroEtapa = etapaDTO.getNumeroEtapa();
        this.fechaInicio = etapaDTO.getFechaInicio();
        this.fechaFin = etapaDTO.getFechaFin();
        this.idTorneo = idTorneo;
    }
}
