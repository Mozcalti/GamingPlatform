package com.mozcalti.gamingapp.model.correos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class DatosCorreoBatallaDTO {

    private String fecha;
    private String horaInicio;
    private String horaFin;
    private Integer rondas;

    private String mailToParticipantes;

    private List<String> participantes;

    public DatosCorreoBatallaDTO(String fecha, String horaInicio, String horaFin, Integer rondas) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.rondas = rondas;
    }

}
