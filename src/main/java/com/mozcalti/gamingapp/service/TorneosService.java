package com.mozcalti.gamingapp.service;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Torneos;
import com.mozcalti.gamingapp.model.batallas.BatallaFechaHoraInicioDTO;
import com.mozcalti.gamingapp.model.batallas.BatallaParticipanteDTO;
import com.mozcalti.gamingapp.model.participantes.EquiposDTO;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;

import java.util.List;

public interface TorneosService extends GenericServiceAPI<Torneos, Integer> {

    List<BatallaFechaHoraInicioDTO> obtieneFechasBatalla(Integer idEtapa, Integer numeroFechas) throws ValidacionException;

    EquiposDTO obtieneInstitucionEquipos() throws ValidacionException;

    List<List<BatallaParticipanteDTO>> obtieneParticipantes(List<Integer> idEquipos, Integer numCompetidores) throws ValidacionException;

    BatallaParticipanteDTO obtieneParticipantes(Integer idEquipo) throws ValidacionException;

    List<Torneos> getTorneos() throws ValidacionException;

    void guardaTorneo(TorneoDTO torneoDTO) throws ValidacionException;

    TorneoDTO obtieneTorneos();

    void modificaTorneo(TorneoDTO torneoDTO) throws ValidacionException;

    void eliminaTorneo();

    void persisteEtapas(List<EtapaDTO> etapasDTOS, boolean esAlta) throws ValidacionException;

    void guardaEtapas(List<EtapaDTO> etapasDTOS) throws ValidacionException;

    List<EtapaDTO> obtieneEtapas() throws ValidacionException;

    void eliminarEtapas();

    void modificaEtapas(List<EtapaDTO> etapasDTOS) throws ValidacionException;

}
