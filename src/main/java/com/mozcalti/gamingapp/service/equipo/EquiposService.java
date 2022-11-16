package com.mozcalti.gamingapp.service.equipo;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Equipos;

public interface EquiposService extends GenericServiceAPI<Equipos, Integer> {
    Integer getIdEquipoByIdParticipante(Integer idEtapas, Integer idParticipante) throws ValidacionException;

}
