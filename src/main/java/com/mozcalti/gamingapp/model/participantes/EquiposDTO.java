package com.mozcalti.gamingapp.model.participantes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class EquiposDTO {

    List<InstitucionEquiposDTO> equiposByInstitucion;
    private List<Integer> idEquipos;

    public EquiposDTO() {
        this.idEquipos = new ArrayList<>();
    }
}
