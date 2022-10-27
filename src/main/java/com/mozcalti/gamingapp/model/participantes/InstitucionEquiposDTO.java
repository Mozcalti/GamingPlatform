package com.mozcalti.gamingapp.model.participantes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class InstitucionEquiposDTO {

    private Integer idInstitucion;
    private List<Integer> idEquipos;

}
