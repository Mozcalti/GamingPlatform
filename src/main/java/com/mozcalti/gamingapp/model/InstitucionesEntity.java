package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "instituciones")
public class InstitucionesEntity {
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
    @Basic
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Basic
    @Column(name = "id_archivo")
    private Integer idArchivo;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", insertable = false, updatable = false)
    private UsuariosEntity usuariosByIdUsuario;
    @ManyToOne
    @JoinColumn(name = "id_archivo", referencedColumnName = "id_archivo", insertable = false, updatable = false)
    private ArchivosCargaEntity archivosCargaByIdArchivo;
    @OneToMany(mappedBy = "institucionesByIdInstitucion")
    private Collection<ParticipantesEntity> participantesByIdInstitucion;

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

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstitucionesEntity that = (InstitucionesEntity) o;
        return idInstitucion == that.idInstitucion && Objects.equals(nombre, that.nombre) && Objects.equals(fechaCreacion, that.fechaCreacion) && Objects.equals(correo, that.correo) && Arrays.equals(logo, that.logo) && Objects.equals(idUsuario, that.idUsuario) && Objects.equals(idArchivo, that.idArchivo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idInstitucion, nombre, fechaCreacion, correo, idUsuario, idArchivo);
        result = 31 * result + Arrays.hashCode(logo);
        return result;
    }

    public UsuariosEntity getUsuariosByIdUsuario() {
        return usuariosByIdUsuario;
    }

    public void setUsuariosByIdUsuario(UsuariosEntity usuariosByIdUsuario) {
        this.usuariosByIdUsuario = usuariosByIdUsuario;
    }

    public ArchivosCargaEntity getArchivosCargaByIdArchivo() {
        return archivosCargaByIdArchivo;
    }

    public void setArchivosCargaByIdArchivo(ArchivosCargaEntity archivosCargaByIdArchivo) {
        this.archivosCargaByIdArchivo = archivosCargaByIdArchivo;
    }

    public Collection<ParticipantesEntity> getParticipantesByIdInstitucion() {
        return participantesByIdInstitucion;
    }

    public void setParticipantesByIdInstitucion(Collection<ParticipantesEntity> participantesByIdInstitucion) {
        this.participantesByIdInstitucion = participantesByIdInstitucion;
    }
}
