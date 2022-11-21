package com.mozcalti.gamingapp.service.torneo;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Torneos;
import com.mozcalti.gamingapp.model.batallas.BatallaFechaHoraInicioDTO;
import com.mozcalti.gamingapp.model.batallas.BatallaParticipanteDTO;
import com.mozcalti.gamingapp.model.correos.DatosCorreoBatallaDTO;
import com.mozcalti.gamingapp.model.participantes.EquiposDTO;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;

import java.util.List;

public interface TorneosService extends GenericServiceAPI<Torneos, Integer> {

    List<BatallaFechaHoraInicioDTO> obtieneFechasBatalla(Integer idEtapa, Integer numeroFechas) throws ValidacionException;

    EquiposDTO obtieneInstitucionEquipos(int idEtapa) throws ValidacionException;

    List<List<BatallaParticipanteDTO>> obtieneParticipantes(List<Integer> idEquipos, Integer numCompetidores) throws ValidacionException;

    BatallaParticipanteDTO obtieneParticipantes(Integer idEquipo) throws ValidacionException;

    List<Torneos> getTorneos() throws ValidacionException;

    void guardaTorneo(TorneoDTO torneoDTO) throws ValidacionException;

    List<TorneoDTO> obtieneTorneos();

    TorneoDTO obtieneTorneos(Integer idTorneo);

    void modificaTorneo(TorneoDTO torneoDTO) throws ValidacionException;

    void eliminaTorneo(Integer idTorneo);

    void guardaEtapas(Integer idTorneo, List<EtapaDTO> etapaDTOS) throws ValidacionException;

    List<EtapaDTO> obtieneEtapas(Integer idTorneo) throws ValidacionException;

    void eliminarEtapas(Integer idTorneo);

    void modificaEtapas(Integer idTorneo, List<EtapaDTO> etapasDTOS) throws ValidacionException;

    List<DatosCorreoBatallaDTO> getDatosCorreoBatalla() throws ValidacionException;

    EtapaDTO obtieneEtapa(Integer idEtapa) throws ValidacionException;

    void modificaEtapa(Integer idEtapa, EtapaDTO etapaDTO);

}
