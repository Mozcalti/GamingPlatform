package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "archivos_carga")
public class ArchivosCargaEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_archivo")
    private int idArchivo;
    @Basic
    @Column(name = "archivo")
    private byte[] archivo;
    @Basic
    @Column(name = "fecha_carga")
    private Date fechaCarga;
    @Basic
    @Column(name = "proceso")
    private String proceso;
    @OneToMany(mappedBy = "archivosCargaByIdArchivo")
    private Collection<InstitucionesEntity> institucionesByIdArchivo;

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArchivosCargaEntity that = (ArchivosCargaEntity) o;
        return idArchivo == that.idArchivo && Arrays.equals(archivo, that.archivo) && Objects.equals(fechaCarga, that.fechaCarga) && Objects.equals(proceso, that.proceso);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idArchivo, fechaCarga, proceso);
        result = 31 * result + Arrays.hashCode(archivo);
        return result;
    }

    public Collection<InstitucionesEntity> getInstitucionesByIdArchivo() {
        return institucionesByIdArchivo;
    }

    public void setInstitucionesByIdArchivo(Collection<InstitucionesEntity> institucionesByIdArchivo) {
        this.institucionesByIdArchivo = institucionesByIdArchivo;
    }
}
