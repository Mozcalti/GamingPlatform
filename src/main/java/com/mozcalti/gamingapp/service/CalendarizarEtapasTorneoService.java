package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.request.torneo.TorneoRequest;
import com.mozcalti.gamingapp.response.batalla.BatallasResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.List;

public interface CalendarizarEtapasTorneoService {

    public void saveTorneo(TorneoRequest torneoRequest) throws SQLException, ValidacionException;

    public BatallasResponse generaBatallas(Integer idEtapa) throws ValidacionException;

    public BatallasResponse saveBatallas(@RequestBody BatallasResponse batallasResponse) throws ValidacionException;

    public List<DatosCorreoBatallaDTO> getDatosCorreoBatalla()  throws ValidacionException;

}
