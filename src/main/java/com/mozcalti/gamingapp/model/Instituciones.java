package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "instituciones")
public class Instituciones {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_institucion")
    private int idInstitucion;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @Basic
    @Column(name = "correo")
    private String correo;
    @Basic
    @Column(name = "logo")
    private byte[] logo;
    @OneToMany(mappedBy = "institucionesByIdInstitucion")
    private Collection<Participantes> participantesByIdInstitucion;
    @OneToMany(mappedBy = "institucionesByIdInstitucion")
    private Collection<Usuarios> usuariosByIdInstitucion;

    public int getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(int idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instituciones that = (Instituciones) o;
        return idInstitucion == that.idInstitucion && Objects.equals(nombre, that.nombre) && Objects.equals(fechaCreacion, that.fechaCreacion) && Objects.equals(correo, that.correo) && Arrays.equals(logo, that.logo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idInstitucion, nombre, fechaCreacion, correo);
        result = 31 * result + Arrays.hashCode(logo);
        return result;
    }

    public Collection<Participantes> getParticipantesByIdInstitucion() {
        return participantesByIdInstitucion;
    }

    public void setParticipantesByIdInstitucion(Collection<Participantes> participantesByIdInstitucion) {
        this.participantesByIdInstitucion = participantesByIdInstitucion;
    }

    public Collection<Usuarios> getUsuariosByIdInstitucion() {
        return usuariosByIdInstitucion;
    }

    public void setUsuariosByIdInstitucion(Collection<Usuarios> usuariosByIdInstitucion) {
        this.usuariosByIdInstitucion = usuariosByIdInstitucion;
    }
}
