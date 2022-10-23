package com.mozcalti.gamingapp.model;

import lombok.*;

import javax.persistence.*;


@Entity
@Data
@Table(name = "robots")
@NoArgsConstructor
public class Robots {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_robot")
    private int idRobot;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "activo")
    private Integer activo;
    @Basic
    @Column(name = "id_equipo")
    private Integer idEquipo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", referencedColumnName = "id_equipo", insertable = false, updatable = false)
    private Equipos equiposByIdEquipo;
}
