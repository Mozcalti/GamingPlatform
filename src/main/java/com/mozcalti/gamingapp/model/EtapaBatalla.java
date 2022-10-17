package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public EtapaBatalla(Integer idEtapa, Integer idBatalla) {
        this.idEtapa = idEtapa;
        this.idBatalla = idBatalla;
    }
}
