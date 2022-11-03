package com.mozcalti.gamingapp.service.batallas;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.BatallaParticipantes;
import com.mozcalti.gamingapp.model.Batallas;
import com.mozcalti.gamingapp.model.catalogos.EtapasDTO;

import java.util.List;

public interface BatallasService extends GenericServiceAPI<Batallas, Integer> {
    void ejecutaBatalla();

    String obtieneRobots(List<BatallaParticipantes> batallaParticipantes);

    List<EtapasDTO> getEtapas() throws ValidacionException;

}
