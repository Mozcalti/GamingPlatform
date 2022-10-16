package com.mozcalti.gamingapp.model.torneos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EquipoDTO {

    private String nombreEquipo;
    private List<Integer> participantes;

    @JsonIgnore
    private int bndCambioEquipo;

}
