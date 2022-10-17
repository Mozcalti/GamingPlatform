package com.mozcalti.gamingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "etapa_batalla")
@Data
@NoArgsConstructor
public class EtapaBatalla {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_etapa_batalla")
    private int idEtapaBatalla;

    @Basic
    @Column(name = "id_etapa")
    private Integer idEtapa;

    @Basic
    @Column(name = "id_batalla")
    private Integer idBatalla;

    @ManyToOne
    @JoinColumn(name = "id_etapa", referencedColumnName = "id_etapa", insertable = false, updatable = false)
    private Etapas etapasByIdEtapa;

    @ManyToOne
    @JoinColumn(name = "id_batalla", referencedColumnName = "id_batalla", insertable = false, updatable = false)
    private Batallas batallasByIdBatalla;

    public EtapaBatalla(Integer idEtapa, Integer idBatalla) {
        this.idEtapa = idEtapa;
        this.idBatalla = idBatalla;
    }
}
