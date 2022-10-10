package com.mozcalti.gamingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "perfiles")
public class PerfilesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_perfil")
    private int idPerfil;
    @Basic
    @Column(name = "perfil")
    private String perfil;
    @OneToMany(mappedBy = "perfilesByIdPerfil")
    @JsonIgnore
    private Collection<UsuariosEntity> usuariosByIdPerfil;

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerfilesEntity that = (PerfilesEntity) o;
        return idPerfil == that.idPerfil && Objects.equals(perfil, that.perfil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPerfil, perfil);
    }

    public Collection<UsuariosEntity> getUsuariosByIdPerfil() {
        return usuariosByIdPerfil;
    }

    public void setUsuariosByIdPerfil(Collection<UsuariosEntity> usuariosByIdPerfil) {
        this.usuariosByIdPerfil = usuariosByIdPerfil;
    }
}
