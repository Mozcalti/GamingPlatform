package com.mozcalti.gamingapp.model.catalogos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ParticipanteDTO {

    private Integer idParticipante;
    private String nombre;

    public ParticipanteDTO(Integer idParticipante) {
        this.idParticipante = idParticipante;
    }
}
