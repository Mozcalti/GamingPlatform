package com.mozcalti.gamingapp.model.batallas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BatallaDTO {

    private Integer idEtapa;
    private Integer idInstitucion;
    private Integer idBatalla;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private List<BatallaParticipanteDTO> batallaParticipantes;
    private Integer rondas;

    public BatallaDTO() {
        this.batallaParticipantes = new ArrayList<>();
    }
    public BatallaDTO(BatallaFechaHoraInicioDTO batallaFechaHoraInicioDTO) {
        this.fecha = batallaFechaHoraInicioDTO.getFecha();
        this.horaInicio = batallaFechaHoraInicioDTO.getHoraInicio();
        this.horaFin = batallaFechaHoraInicioDTO.getHoraFin();
    }
}
