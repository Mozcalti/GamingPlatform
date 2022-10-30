package com.mozcalti.gamingapp.model.torneos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mozcalti.gamingapp.model.Etapas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EtapaDTO {

    private Integer idEtapa;
    private Integer numeroEtapa;
    private String fechaInicio;
    private String fechaFin;
    private ReglasDTO reglas;

    @JsonIgnore
    private List<ParticipanteDTO> participantes;

    @JsonIgnore
    private List<EquipoDTO> equipos;

    public EtapaDTO(Etapas etapa) {
        this.idEtapa = etapa.getIdEtapa();
        this.numeroEtapa = etapa.getNumeroEtapa();
        this.fechaInicio = etapa.getFechaInicio();
        this.fechaFin = etapa.getFechaFin();
        this.reglas = new ReglasDTO(etapa.getReglas());
    }
}
