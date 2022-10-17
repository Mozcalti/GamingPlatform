package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public Integer getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuarios that = (Usuarios) o;
        return idUsuario == that.idUsuario && Objects.equals(nombre, that.nombre) && Objects.equals(apellidos, that.apellidos) && Objects.equals(correo, that.correo) && Objects.equals(idPerfil, that.idPerfil) && Objects.equals(idInstitucion, that.idInstitucion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, nombre, apellidos, correo, idPerfil, idInstitucion);
    }

    public Perfiles getPerfilesByIdPerfil() {
        return perfilesByIdPerfil;
    }

    public void setPerfilesByIdPerfil(Perfiles perfilesByIdPerfil) {
        this.perfilesByIdPerfil = perfilesByIdPerfil;
    }

    public Instituciones getInstitucionesByIdInstitucion() {
        return institucionesByIdInstitucion;
    }

    public void setInstitucionesByIdInstitucion(Instituciones institucionesByIdInstitucion) {
        this.institucionesByIdInstitucion = institucionesByIdInstitucion;
    }
}
