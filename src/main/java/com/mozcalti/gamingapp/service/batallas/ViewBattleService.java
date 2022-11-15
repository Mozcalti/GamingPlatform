package com.mozcalti.gamingapp.service.batallas;

import com.mozcalti.gamingapp.model.BatallaParticipantes;
import com.mozcalti.gamingapp.model.batallas.view.BatallaViewDTO;

import java.util.List;

public interface ViewBattleService {

    BatallaViewDTO obtieneDatosViewBattle(String idBatalla);

    String nombreParticipanteRobotActivo(List<BatallaParticipantes> batallaParticipantes);

}
