package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.batallas.resultado.ResultadosDTO;
import com.mozcalti.gamingapp.model.dto.TablaDTO;
import com.mozcalti.gamingapp.service.resultados.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboads")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DashboadsController {

    private DashboardService dashboardService;

    @GetMapping("/global")
    public TablaDTO<ResultadosDTO> todosResultados(@RequestParam String texto, @RequestParam Integer indice){
        return dashboardService.listaResultadosBatalla(texto,indice);
    }

}
