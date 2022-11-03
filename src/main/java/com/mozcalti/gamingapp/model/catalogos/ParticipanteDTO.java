package com.mozcalti.gamingapp.model.catalogos;

import com.mozcalti.gamingapp.model.Participantes;
import com.mozcalti.gamingapp.utils.Constantes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ParticipanteDTO {

    private Integer idParticipante;
    private String nombre;

    public ParticipanteDTO(Participantes participantes) {
        this.idParticipante = participantes.getIdParticipante();
        this.nombre = participantes.getNombre().trim() + Constantes.ESPACIO + participantes.getApellidos().trim();
    }
}
