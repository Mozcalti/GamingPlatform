package com.mozcalti.gamingapp.model.torneos;

import com.mozcalti.gamingapp.model.Torneos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TorneoDTO {

    private Integer idTorneo;
    private String fechaInicio;
    private String fechaFin;

    private List<HoraHabilDTO> horasHabiles;
    private Integer numEtapas;

    private List<EtapaDTO> etapas;

    public TorneoDTO(Torneos torneos, Integer idTorneo) {
        this.idTorneo = idTorneo;
        this.fechaInicio = torneos.getFechaInicio();
        this.fechaFin = torneos.getFechaFin();
        this.numEtapas = torneos.getNumEtapas();
    }
}

