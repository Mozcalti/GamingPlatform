package com.mozcalti.gamingapp.service.dashboard;

import com.mozcalti.gamingapp.model.BatallaParticipantes;
import com.mozcalti.gamingapp.model.Batallas;
import com.mozcalti.gamingapp.model.Resultados;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosParticipantesDTO;
import com.mozcalti.gamingapp.model.dto.DetalleBatallaDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;

import java.util.Collection;
import java.util.List;

public interface DashboardService {

    void buscaSalidaBatallas(Batallas batalla);

    TablaDTO<ResultadosDTO> listaResultadosBatalla(Integer indice);

    List<ResultadosParticipantesDTO> listaResultadosParticipantesBatalla(Integer idEtapa, String idInstitucion);

    List<ResultadosInstitucionGpoDTO> gruopResultadosParticipantesBatalla(Integer idEtapa);

    List<DetalleBatallaDTO> listaDetalleBatallasIndividuales(Integer idEtapa, String idInstitucion);

    List<EtapaDTO> listaEtapas();

    Integer idInstitucion(Resultados resultados);

}
