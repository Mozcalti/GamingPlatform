package com.mozcalti.gamingapp.model.torneos;

import com.mozcalti.gamingapp.model.TorneoHorasHabiles;
import com.mozcalti.gamingapp.model.Torneos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
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

    public TorneoDTO(Torneos torneos) {
        this.idTorneo = torneos.getIdTorneo();
        this.fechaInicio = torneos.getFechaInicio();
        this.fechaFin = torneos.getFechaFin();
        this.numEtapas = torneos.getNumEtapas();

        this.horasHabiles = new ArrayList<>();
        for(TorneoHorasHabiles torneoHoraHabil : torneos.getTorneoHorasHabilesByIdTorneo()) {
            horasHabiles.add(new HoraHabilDTO(torneoHoraHabil));
        }
    }

    public TorneoDTO(Integer idTorneo, String fechaInicio, String fechaFin, Integer numEtapas,
                     Collection<TorneoHorasHabiles> torneoHorasHabiles) {
        this.idTorneo = idTorneo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numEtapas = numEtapas;

        this.horasHabiles = new ArrayList<>();
        for(TorneoHorasHabiles torneoHoraHabil : torneoHorasHabiles) {
            horasHabiles.add(new HoraHabilDTO(torneoHoraHabil));
        }
    }

}

