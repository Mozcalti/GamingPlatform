package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "participantes")
public class ParticipantesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_participante")
    private int idParticipante;
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
    @Column(name = "carrera")
    private String carrera;
    @Basic
    @Column(name = "foto")
    private byte[] foto;
    @Basic
    @Column(name = "academia")
    private String academia;
    @Basic
    @Column(name = "semestre")
    private Integer semestre;
    @Basic
    @Column(name = "id_institucion")
    private Integer idInstitucion;
    @OneToMany(mappedBy = "participantesByIdParticipante")
    private Collection<ParticipanteEquipoEntity> participanteEquiposByIdParticipante;
    @ManyToOne
    @JoinColumn(name = "id_institucion", referencedColumnName = "id_institucion", insertable = false, updatable = false)
    private InstitucionesEntity institucionesByIdInstitucion;

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
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

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getAcademia() {
        return academia;
    }

    public void setAcademia(String academia) {
        this.academia = academia;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
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
        ParticipantesEntity that = (ParticipantesEntity) o;
        return idParticipante == that.idParticipante && Objects.equals(nombre, that.nombre) && Objects.equals(apellidos, that.apellidos) && Objects.equals(correo, that.correo) && Objects.equals(carrera, that.carrera) && Arrays.equals(foto, that.foto) && Objects.equals(academia, that.academia) && Objects.equals(semestre, that.semestre) && Objects.equals(idInstitucion, that.idInstitucion);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idParticipante, nombre, apellidos, correo, carrera, academia, semestre, idInstitucion);
        result = 31 * result + Arrays.hashCode(foto);
        return result;
    }

    public Collection<ParticipanteEquipoEntity> getParticipanteEquiposByIdParticipante() {
        return participanteEquiposByIdParticipante;
    }

    public void setParticipanteEquiposByIdParticipante(Collection<ParticipanteEquipoEntity> participanteEquiposByIdParticipante) {
        this.participanteEquiposByIdParticipante = participanteEquiposByIdParticipante;
    }

    public InstitucionesEntity getInstitucionesByIdInstitucion() {
        return institucionesByIdInstitucion;
    }

    public void setInstitucionesByIdInstitucion(InstitucionesEntity institucionesByIdInstitucion) {
        this.institucionesByIdInstitucion = institucionesByIdInstitucion;
    }
}
