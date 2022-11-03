package com.mozcalti.gamingapp.service.batallas;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.model.BatallaParticipantes;
import com.mozcalti.gamingapp.model.Batallas;

import java.util.List;

public interface BatallasService extends GenericServiceAPI<Batallas, Integer> {
    void ejecutaBatalla();

    String obtieneRobots(List<BatallaParticipantes> batallaParticipantes);

}
