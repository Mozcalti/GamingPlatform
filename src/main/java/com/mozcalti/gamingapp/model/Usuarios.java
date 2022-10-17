package com.mozcalti.gamingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuarios {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_usuario")
    private int idUsuario;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "apellidos")
    private String apellidos;
    @Basic
    @Column(name = "correo")
    private String correo;
    @Basic
    @Column(name = "id_perfil")
    private Integer idPerfil;
    @Basic
    @Column(name = "id_institucion")
    private Integer idInstitucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil", insertable = false, updatable = false)
    private Perfiles perfilesByIdPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_institucion", referencedColumnName = "id_institucion", insertable = false, updatable = false)
    private Instituciones institucionesByIdInstitucion;

}
