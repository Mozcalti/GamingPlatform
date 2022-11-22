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
    public List<TorneoDTO> obtieneTorneos() {
        return torneosService.obtieneTorneos();
    }

    @GetMapping("/consultar/{idTorneo}")
    public TorneoDTO obtieneTorneos(@PathVariable Integer idTorneo) {
        return torneosService.obtieneTorneos(idTorneo);
    }

    @PutMapping("/cambiar")
    public void modificaTorneo(@RequestBody TorneoDTO torneoDTO) {
        torneosService.modificaTorneo(torneoDTO);
    }

    @DeleteMapping("/eliminar/{idTorneo}")
    public void eliminaTorneo(@PathVariable Integer idTorneo) {
        torneosService.eliminaTorneo(idTorneo);
    }

    @PostMapping("/etapas/guardar/{idTorneo}")
    public void guardaEtapas(@PathVariable Integer idTorneo,
                             @RequestBody List<EtapaDTO> etapaDTOS) {
        torneosService.guardaEtapas(idTorneo, etapaDTOS);
    }

    @GetMapping("/etapas/consultar/{idTorneo}")
    public List<EtapaDTO> obtieneEtapas(@PathVariable Integer idTorneo) {
        return torneosService.obtieneEtapas(idTorneo);
    }

    @DeleteMapping("/etapas/eliminar/{idTorneo}")
    public void eliminaEtapas(@PathVariable Integer idTorneo) {
        torneosService.eliminarEtapas(idTorneo);
    }

    @GetMapping("/etapa/consulta/{idEtapa}")
    public EtapaDTO obtieneEtapa(@PathVariable Integer idEtapa) {
        return torneosService.obtieneEtapa(idEtapa);
    }

    @PutMapping("/etapa/cambiar/{idEtapa}")
    public void modificaEtapa(@PathVariable Integer idEtapa, @RequestBody EtapaDTO etapaDTO) {
        torneosService.modificaEtapa(idEtapa, etapaDTO);
    }

}
