package com.mozcalti.gamingapp.model;


import javax.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cat_institucion")
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "nombre") private String nombre;
    @Column(name = "correo") private String correo;
    @Column(name = "fecha_creacion") private String fechaCreacion;

}
