package com.mozcalti.gamingapp.controller;

import com.mozcalti.gamingapp.model.torneos.EtapaDTO;
import com.mozcalti.gamingapp.model.torneos.TorneoDTO;
import com.mozcalti.gamingapp.service.torneo.TorneosService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/torneo")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TorneoControlller {
    private TorneosService torneosService;

    @PostMapping("/guardar")
    public void guardaTorneo(@RequestBody TorneoDTO torneoDTO) {
        torneosService.guardaTorneo(torneoDTO);
    }

    @GetMapping("/consultar")
    public TorneoDTO obtieneTorneos() {
        return torneosService.obtieneTorneos();
    }

    @PutMapping("/cambiar")
    public void modificaTorneo(@RequestBody TorneoDTO torneoDTO) {
        torneosService.modificaTorneo(torneoDTO);
    }

    @DeleteMapping("/eliminar")
    public void eliminaTorneo() {
        torneosService.eliminaTorneo();
    }

    @PostMapping("/etapas/guardar")
    public void guardaEtapas(@RequestBody List<EtapaDTO> etapasDTOS) {
        torneosService.guardaEtapas(etapasDTOS);
    }

    @GetMapping("/etapas/consultar")
    public List<EtapaDTO> obtieneEtapas() {
        return torneosService.obtieneEtapas();
    }

    @DeleteMapping("/etapas/eliminar")
    public void eliminaEtapas() {
        torneosService.eliminarEtapas();
    }

    @PutMapping("/etapas/cambiar")
    public void modificaTorneo(@RequestBody List<EtapaDTO> etapasDTOS) {
        torneosService.modificaEtapas(etapasDTOS);
    }

}
