package com.mozcalti.gamingapp.model.batallas.resultado;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadosInstitucionGpoDTO {

    private String nombreInstitucion;
    List<ResultadosParticipantesGpoDTO> participantes;

}
