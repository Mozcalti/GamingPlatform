package com.mozcalti.gamingapp.model.dto;


import lombok.Getter;

@Getter
public class InstitucionDTO {

    private final String nombre;
    private final String correo;
    private final String fechaCreacion;

    public InstitucionDTO(String nombre, String correo, String fechaCreacion) {
        this.nombre = nombre;
        this.correo = correo;
        this.fechaCreacion = fechaCreacion;
    }

}
