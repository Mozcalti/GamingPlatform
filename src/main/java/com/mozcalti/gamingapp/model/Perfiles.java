package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "perfiles")
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
        Perfiles that = (Perfiles) o;
        return idPerfil == that.idPerfil && Objects.equals(perfil, that.perfil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPerfil, perfil);
    }

    public Collection<Usuarios> getUsuariosByIdPerfil() {
        return usuariosByIdPerfil;
    }

    public void setUsuariosByIdPerfil(Collection<Usuarios> usuariosByIdPerfil) {
        this.usuariosByIdPerfil = usuariosByIdPerfil;
    }
}
