package com.mozcalti.gamingapp.model.batallas;

import com.mozcalti.gamingapp.model.BatallaParticipantes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatallaParticipanteDTO {

    private Integer idParticipante;
    private String nombre;

    public BatallaParticipanteDTO(BatallaParticipantes batallaParticipantes) {
        this.idParticipante = batallaParticipantes.getIdParticipanteEquipo();
        this.nombre = batallaParticipantes.getNombre();
    }
}
