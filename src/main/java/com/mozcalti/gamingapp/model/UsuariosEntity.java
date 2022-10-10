package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
public class UsuariosEntity {
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
    @OneToMany(mappedBy = "usuariosByIdUsuario")
    private Collection<InstitucionesEntity> institucionesByIdUsuario;
    @ManyToOne
    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil", insertable = false, updatable = false)
    private PerfilesEntity perfilesByIdPerfil;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuariosEntity that = (UsuariosEntity) o;
        return idUsuario == that.idUsuario && Objects.equals(nombre, that.nombre) && Objects.equals(apellidos, that.apellidos) && Objects.equals(correo, that.correo) && Objects.equals(idPerfil, that.idPerfil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, nombre, apellidos, correo, idPerfil);
    }

    public Collection<InstitucionesEntity> getInstitucionesByIdUsuario() {
        return institucionesByIdUsuario;
    }

    public void setInstitucionesByIdUsuario(Collection<InstitucionesEntity> institucionesByIdUsuario) {
        this.institucionesByIdUsuario = institucionesByIdUsuario;
    }

    public PerfilesEntity getPerfilesByIdPerfil() {
        return perfilesByIdPerfil;
    }

    public void setPerfilesByIdPerfil(PerfilesEntity perfilesByIdPerfil) {
        this.perfilesByIdPerfil = perfilesByIdPerfil;
    }
}
