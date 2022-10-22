package com.mozcalti.gamingapp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "table_prueba", schema = "public", catalog = "gaming")
public class TablePruebaEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "nombre")
    private Integer nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TablePruebaEntity that = (TablePruebaEntity) o;
        return id == that.id && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }
}
