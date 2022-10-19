package com.mozcalti.gamingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "perfiles")
@Data
@NoArgsConstructor
public class Perfiles {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_perfil")
    private int idPerfil;
    @Basic
    @Column(name = "perfil")
    private String perfil;
    @OneToMany(mappedBy = "perfilesByIdPerfil")
    private Collection<Usuarios> usuariosByIdPerfil;

}
