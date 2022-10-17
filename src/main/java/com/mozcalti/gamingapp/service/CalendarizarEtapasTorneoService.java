package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;

import java.util.List;

public interface CalendarizarEtapasTorneoService {

    public void saveTorneo(TorneoDTO torneoDTO) throws ValidacionException;

    public TorneoDTO getTorneo(int idTorneo) throws ValidacionException;

    public void updateTorneo(TorneoDTO torneoDTO) throws ValidacionException;

    public void deleteTorneo(int idTorneo) throws ValidacionException;

    public BatallasDTO generaBatallas(Integer idEtapa) throws ValidacionException;

    public void saveBatallas(BatallasDTO batallasDTO) throws ValidacionException;

    public List<DatosCorreoBatallaDTO> getDatosCorreoBatalla() throws ValidacionException;

    public BatallasDTO getBatallas(Integer idEtapa) throws ValidacionException;

    public void updateBatallas(BatallasDTO batallasDTO) throws ValidacionException;

    public void deleteBatallas(Integer idEtapa) throws ValidacionException;

}
