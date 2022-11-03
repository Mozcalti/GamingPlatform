package com.mozcalti.gamingapp.service.torneo;

import com.mozcalti.gamingapp.commons.GenericServiceAPI;
import com.mozcalti.gamingapp.exceptions.ValidacionException;
import com.mozcalti.gamingapp.model.Etapas;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;

import java.util.List;

public interface EtapasService extends GenericServiceAPI<Etapas, Integer> {

    List<ResultadosInstitucionGpoDTO> obtieneParticipantes(Integer idEtapa);
    Etapas obtieneSiguieteEtapa(Integer idEtapa);
    void finEtapaInstitucion(Integer idEtapa, Integer numParticipantes) throws ValidacionException;
    void finEtapaGlobal(Integer numParticipantes) throws ValidacionException;

}
