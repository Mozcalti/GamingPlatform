package com.mozcalti.gamingapp.model.batallas.resultado;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResultadoPorDiaDTO {

    private String fecha;
    private Integer numeroEtapa;
    private Double score;
    private String nombre;
    private String robot;

}
