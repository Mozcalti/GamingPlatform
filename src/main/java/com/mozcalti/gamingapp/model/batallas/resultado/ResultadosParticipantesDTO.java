package com.mozcalti.gamingapp.model.batallas.resultado;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadosParticipantesDTO {

    private Integer idParticipante;
    private String nombreParticipantes;
    private Integer idInstitucion;
    private String nombreInstitucion;
    private String nombreRobot;
    private Double score;

}
