package com.mozcalti.gamingapp.model.batallas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BatallaFechaHoraInicioDTO {

    private int id;
    private String fecha;
    private String horaInicio;
    private String horaFin;

    public BatallaFechaHoraInicioDTO(String fecha, String horaInicio, String horaFin) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

}
