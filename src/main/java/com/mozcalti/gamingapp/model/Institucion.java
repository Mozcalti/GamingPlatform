package com.mozcalti.gamingapp.model;


import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "institucion")
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nombre") private String nombre;
    @Column(name = "correo") private String correo;
    @Column(name = "fecha_creacion") private LocalDateTime fechaCreacion;
    @Column(name = "logo") private String logo;

}
