package com.mozcalti.gamingapp.model.torneos;

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

    private List<ParticipanteDTO> participantes;

    private List<EquipoDTO> equipos;

    public EtapaDTO(Etapas etapas) {
        this.idEtapa = etapas.getIdEtapa();
        this.numeroEtapa = etapas.getNumeroEtapa();
        this.fechaInicio = etapas.getFechaInicio();
        this.fechaFin = etapas.getFechaFin();
        this.reglas = new ReglasDTO(etapas.getReglas());
    }
}
