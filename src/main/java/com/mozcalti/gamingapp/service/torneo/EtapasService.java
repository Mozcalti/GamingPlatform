package com.mozcalti.gamingapp.service.torneo;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Etapas;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosParticipantesDTO;

import java.util.List;

public interface EtapasService extends GenericServiceAPI<Etapas, Integer> {

    List<ResultadosInstitucionGpoDTO> obtieneParticipantesByInstitucion(Integer idEtapa);
    Etapas obtieneSiguienteEtapa(Integer idEtapa);
    void desactivaEquipos();
    void finEtapaInstitucion(Integer idEtapa, Integer numParticipantes) throws ValidacionException;

    List<ResultadosParticipantesDTO> obtieneParticipantes(Integer idEtapa);
    void finEtapaGlobal(Integer idEtapa, Integer numParticipantes) throws ValidacionException;

}
