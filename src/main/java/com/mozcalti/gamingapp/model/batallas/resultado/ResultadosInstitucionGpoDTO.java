package com.mozcalti.gamingapp.model.batallas.resultado;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ResultadosInstitucionGpoDTO {

    private String nombreInstitucion;
    List<ResultadosParticipantesGpoDTO> participantes;
    public ResultadosInstitucionGpoDTO() {
        this.participantes = new ArrayList<>();
    }

}
