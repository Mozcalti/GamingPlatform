package com.mozcalti.gamingapp.model.dto;

import java.util.*;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosDTO;
import com.mozcalti.gamingapp.model.torneos.ReglasDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DetalleBatallaDTO {

    private int idBatalla;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String estatus;
    private ReglasDTO reglasDTO;
    private List<ResultadosDTO> listaResultadosDTO;
}
