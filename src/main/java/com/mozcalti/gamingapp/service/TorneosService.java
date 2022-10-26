package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Torneos;
import com.mozcalti.gamingapp.model.batallas.BatallaFechaHoraInicioDTO;

import java.util.List;

public interface TorneosService extends GenericServiceAPI<Torneos, Integer> {

    List<BatallaFechaHoraInicioDTO> obtieneFechasBatalla(Integer idEtapa, Integer numeroFechas) throws ValidacionException;


}
