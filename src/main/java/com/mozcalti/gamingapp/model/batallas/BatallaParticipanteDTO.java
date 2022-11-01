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

    private Integer idRobot;

    public BatallaParticipanteDTO(BatallaParticipantes batallaParticipantes) {
        this.idParticipante = batallaParticipantes.getIdParticipanteEquipo();
        this.nombre = batallaParticipantes.getNombre();
    }

    public BatallaParticipanteDTO(Integer idParticipante, String nombre) {
        this.idParticipante = idParticipante;
        this.nombre = nombre;
    }

}
