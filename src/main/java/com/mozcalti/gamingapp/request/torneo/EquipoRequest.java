package com.mozcalti.gamingapp.request.torneo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EquipoRequest {

    private String nombreEquipo;
    private List<Integer> participantes;

}
