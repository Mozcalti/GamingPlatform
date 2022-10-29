package com.mozcalti.gamingapp.model.participantes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class EquiposDTO {

    List<InstitucionEquiposDTO> equiposByInstitucion;
    private Set<Integer> idEquipos;

    public EquiposDTO() {
        this.idEquipos = new HashSet<>();
    }
}
