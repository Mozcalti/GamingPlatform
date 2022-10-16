package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.model.batallas.BatallasDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.List;

public interface CalendarizarEtapasTorneoService {

    public void saveTorneo(TorneoDTO torneoDTO) throws SQLException, ValidacionException;

    public TorneoDTO getTorneo(int idTorneo) throws ValidacionException;

    public void updateTorneo(TorneoDTO torneoDTO) throws ValidacionException;

    public void deleteTorneo(int idTorneo) throws ValidacionException;

    public BatallasDTO generaBatallas(Integer idEtapa) throws ValidacionException;

    public BatallasDTO saveBatallas(@RequestBody BatallasDTO batallasDTO) throws ValidacionException;

    public List<DatosCorreoBatallaDTO> getDatosCorreoBatalla()  throws ValidacionException;

}
