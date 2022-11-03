package com.mozcalti.gamingapp.service.batallas;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;

public interface CalendarizarEtapasTorneoService {

    BatallasDTO generaBatallas(Integer idEtapa);

    void saveBatallas(BatallasDTO batallasDTO) throws ValidacionException;

    BatallasDTO getBatallas(Integer idEtapa);

    void updateBatallas(BatallasDTO batallasDTO) throws ValidacionException;

    void deleteBatallas(Integer idEtapa) throws ValidacionException;

}
