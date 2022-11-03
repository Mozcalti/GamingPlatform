package com.mozcalti.gamingapp.service.dashboard;

import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosParticipantesDTO;
import com.mozcalti.gamingapp.model.dto.DetalleBatallaDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;

import java.util.List;

public interface DashboardService {

    void buscaSalidaBatallas();

    TablaDTO<ResultadosDTO> listaResultadosBatalla(Integer indice);

    List<ResultadosParticipantesDTO> listaResultadosParticipantesBatalla(Integer idEtapa, String idInstitucion);

    List<ResultadosInstitucionGpoDTO> gruopResultadosParticipantesBatalla(Integer idEtapa);

    List<DetalleBatallaDTO> listaDetalleBatallasIndividuales(Integer idEtapa);

}