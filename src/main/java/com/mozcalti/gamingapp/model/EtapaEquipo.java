package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "etapa_equipo")
@Data
@NoArgsConstructor
public class EtapaEquipo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_etapa_equipo")
    private int idEtapaEquipo;

    @Basic
    @Column(name = "id_etapa")
    private int idEtapa;

    @Basic
    @Column(name = "id_equipo")
    private int idEquipo;

    public EtapaEquipo(int idEtapa, int idEquipo) {
        this.idEtapa = idEtapa;
        this.idEquipo = idEquipo;
    }

}
