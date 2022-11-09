package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosInstitucionGpoDTO;
import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosParticipantesDTO;
import com.mozcalti.gamingapp.model.dto.DetalleBatallaDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.service.dashboard.DashboardService;
import com.mozcalti.gamingapp.utils.Constantes;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboads")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DashboadsController {

    private DashboardService dashboardService;

    @GetMapping("/global")
    public TablaDTO<ResultadosDTO> todosResultados(@RequestParam Integer indice){
        return dashboardService.listaResultadosBatalla(indice);
    }

    @GetMapping("/consulta")
    public List<ResultadosParticipantesDTO> getResultadosParticipantes(@RequestParam Integer idEtapas,
                                                                       @RequestParam(defaultValue = Constantes.TODOS) String idInstitucion){
        return dashboardService.listaResultadosParticipantesBatalla(idEtapas, idInstitucion);
    }

    @GetMapping("/agrupar")
    public List<ResultadosInstitucionGpoDTO> getGpoResultadosInstituciones(@RequestParam Integer idEtapas){
        return dashboardService.gruopResultadosParticipantesBatalla(idEtapas);
    }

    @GetMapping("/verBatallasIndividuales")
    public List<DetalleBatallaDTO> getDetalleBatallasIndividuales(@RequestParam Integer idEtapa,
                                                                  @RequestParam(defaultValue = Constantes.TODOS) String idInstitucion){
        return dashboardService.listaDetalleBatallasIndividuales(idEtapa, idInstitucion);
    }

    @GetMapping("/obtenerEtapas")
    public List<EtapaDTO> getEtapas(){
        return dashboardService.listaEtapas();
    }

}
